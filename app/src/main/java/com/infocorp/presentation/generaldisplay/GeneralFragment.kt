package com.infocorp.presentation.generaldisplay

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.infocorp.R
import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentGeneralBinding
import com.infocorp.presentation.logindisplay.LoginFragmentDirections
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null

    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw NullPointerException()

    private val firebase by lazy {
        fragmentViewModel.getFirebase()
    }

    private val auth by lazy {
        firebase.auth
    }

    private val firebaseDB by lazy {
        firebase.database.getReference(Constants.GENERAL_DB.value)
    }

    private val fragmentViewModel: GeneralFragmentViewModel by viewModels()

    private var isNetworkAvailable: (() -> Boolean)? = null
    private var updateStateBottomMenu: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isNetworkAvailable = { (activity as MainActivity).isNetworkAvailable() }
        updateStateBottomMenu = { (activity as MainActivity).enableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCurrentUser()
        initViews()
        downloadData()
        onObservers()

         //addCorps()
    }

    private fun downloadData() {
        lifecycleScope.launch(Dispatchers.IO) {

            val listIdFavourite = fragmentViewModel.downloadListIdFavourite()
            val listIdOldCorps = fragmentViewModel.downloadListIdOldCorps()

            val listFromFirebase = mutableListOf<CorporationDto>()

            fragmentViewModel.clearLocalDataBase()

            firebaseDB.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val responseFirebase = snapshot.children

                    responseFirebase.forEach { child ->
                        val corpDto = child.getValue(CorporationDto::class.java)
                        val corpDtoWithChildId = corpDto?.copy(id = child.key.toString())

                        if (corpDtoWithChildId != null) {

                            for (value in listIdFavourite) {
                                if (value == corpDtoWithChildId.id) {
                                    corpDtoWithChildId.isFavourite = true
                                    corpDtoWithChildId.isNew = false
                                }
                            }

                            for (value in listIdOldCorps) {
                                if (value == corpDtoWithChildId.id) {
                                    corpDtoWithChildId.isNew = false
                                }
                            }

                            listFromFirebase.add(corpDtoWithChildId)

                        }
                    }
                    myScope.launch {
                        fragmentViewModel.addAllCorpInDataBase(listFromFirebase)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MyLog", "Error message ${error.message}")
                }
            })
        }
    }

    private fun onObservers() {

        lifecycleScope.launch {
            fragmentViewModel.allCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        if (isNetworkAvailable?.invoke() == false) {
                            shimmerLayout.shimmer.visibility = View.VISIBLE
                            statisticCard.statistic.visibility = View.GONE

                        } else if (isNetworkAvailable?.invoke() == true && it == 0) {
                            shimmerLayout.shimmer.visibility = View.VISIBLE
                            statisticCard.statistic.visibility = View.GONE

                        } else {
                            statisticCard.statistic.visibility = View.VISIBLE
                            shimmerLayout.shimmer.visibility = View.GONE
                            statisticCard.countAllInBase.text = it.toString()
                        }
                    }
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.userCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.statisticCard.countInUserBase.text = it.toString()
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.oldCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.statisticCard.countNewInBase.text = it.toString()
                }
        }

    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser == null) {

            val action = GeneralFragmentDirections.actionGeneralFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            findNavController().popBackStack(R.id.loginFragment, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addCorps() {
//       основная бд
        val databaseParent = Firebase.database.getReference(Constants.GENERAL_DB.value)
//       пользовательская бд
        val databaseChild = Firebase.database.getReference(Constants.USER_DB.value)

//      удаление из пользовательской USER_CORPORATION бд
//     databaseChild.child("-Ny0Mf6qyYQ4PMnx6NDk").removeValue()


//        сравнение из пользовательской бд и добавление в основную

//        databaseChild.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val response = snapshot.children
//                response.forEach { child ->
//                    val childValue = child.getValue(CorporationDto::class.java)
//
//                    if (child.key == "-NxdQoErgl8ch0S9XdSn" ){
//                       databaseParent.push().setValue(childValue)
//                   }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) { TODO() }
//        })

//
//
//        val corp1 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "TECT",
//            poster = "",
//description = "Скайнетикс, ЧУП Частное предприятие Год основания: 2004 Количество сотрудников: 6",
//            address = "220007, г. Минск, ул. Артиллеристов, 11-7",
//            phones = "(017)226-34-34",
//            email = "info@skynetix.com",
//            website = "www.skynetixsoftware.com",
//            notes = "ssssssssssssssss. sssssssssssssssss. sssssssssssssssssss. sssssssssssssssssssssssssss. ssssssssssssssssssssssssssssss.   sssssssssssssssssssssssssssssss"
//        )
//        databaseParent.push().setValue(corp1)
//////////////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Сканвест",
//            poster = "",
//description = "ЗАО Закрытое акционерное общество Год основания: 1994 Количество сотрудников: 12 УНП: 100829693",
//            address = "220024, г. Минск, ул. Кижеватова, 7/2 (офис 02)",
//            phones = "(017)275-59-92",
//            email = "office@scan-west.com",
//            website = "www.scan-west.com"
//        )
//        databaseParent.push().setValue(corp2)
//////////////////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Скенд",
//            poster = "",
//description = "Скенд, ООО Общество с ограниченной ответственностью Год основания: 2000 Количество сотрудников: 90 УНП: 190149789",
//            address = "220035, г. Минск, ул. Тимирязева, 46-26",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp3)
//////////////////////
//////////////////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Смарт Системз",
//            poster = "",
//description = "Смарт Системз, ООО Общество с ограниченной ответственностью Год основания: 2004 Количество сотрудников: 10 УНП: 190587359",
//            address = "220072, г. Минск, ул. Академическая, 16-409",
//            phones = "(017)284-24-19",
//            email = "info@smart-systems.by",
//            website = "www.smart-systems.by"
//        )
//        databaseParent.push().setValue(corp4)
//////////////////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СмартАртери",
//            poster = "http://belorussia.su/com_logo/1542268306logo1_big.jpg",
//description = "Компания ООО СмартАртери является системным интегратором RFID-решений на территории Беларуси. Поставляем RFID-метки и RFID-оборудование. Являемся партнерами LEAN.LT по внедрению технологии  Бережливого производства",
//            address = "Минский район, район д.Боровая 1, Главный корпус, офис 310",
//            phones = "+375293142667, +37525772875",
//            email = "sartrfid@gmail.com",
//            website = "http://sart.by"
//        )
//        databaseParent.push().setValue(corp5)
//////////////////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СмартМьютекс",
//            poster = "http://belorussia.su/com_logo/1416385439logo1_big.jpg",
//description = "Клиентам ООО СмартМьютекс мы предлагаем частичную и комплексную автоматизацию малого и среднего бизнеса на базе разработанной нами платформы JSWP. На данной платформе нами уже были созданы и внедрены эффективные IT-решения по автоматизации бизнес-процессов в таких отраслях как страхование, торговля и техобслуживание транспорта. Полифункциональность созданной нами платформы позволяет автоматизировать бизнес-процессы в любой сфере деловой активности, а опыт существующих разработок экономит время и деньги наших клиентов.",
//            address = "ул. Замковая, 33",
//            phones = "+375 29 692 11 33",
//            email = "marketing@mutex.by",
//            website = "http://mutex.by"
//        )
//        databaseParent.push().setValue(corp6)
//////////////////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Смеш",
//            poster = "",
//description = "Смеш, УП Частное предприятие Год основания: 1994 Количество сотрудников: 10 УНП: 100752247",
//            address = "220121, г. Минск, ул. Притыцкого 60/1-132, а/я 4",
//            phones = "(017)255-71-97",
//            email = "office@smash.by",
//            website = "www.smash.by"
//        )
//        databaseParent.push().setValue(corp7)
//////////////////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СОВРЕМЕННЫЕ ТЕХНОЛОГИИ СВЯЗИ",
//            poster = "http://belorussia.su/com_logo/1338976350logo1_big.jpg",
//description = "Разработка оборудования и электроники. Разработка программного обеспечения. Программное обеспечение для электронного оборудования, мобильных устройств, интерфейсов взаимодействия, под Windows, Linux. Web-приложения и разработка сайтов",
//            address = "222307, Беларусь, г.Молодечно, ул.Строителей, 15а-303а",
//            phones = "+37529 7635707, +37529 6252482",
//            email = "dir@stsby.com",
//            website = "http://stsby.com"
//        )
//        databaseParent.push().setValue(corp8)
//////////////////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Софт-Систем",
//            poster = "",
//description = "Софт-Систем, УП Частное предприятие Год основания: 1997 Количество сотрудников: 5 УНП: 101299606",
//            address = "220039, г. Минск, пр-т Машерова, 20-404",
//            phones = "(017)293-43-40",
//            email = "info@softsys.by.com",
//            website = "http://softsys.by.com"
//        )
//        databaseParent.push().setValue(corp9)
////////////////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Софтех",
//            poster = "",
//description = "Софтех, ООО Общество с ограниченной ответственностью Год основания: 1994 Количество сотрудников: 30 УНП: 101020832",
//            address = "220103, г. Минск, ул. Кнорина, 50",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
//////////////////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Софтлайн",
//            poster = "",
//description = "Софтлайн, ООО Общество с ограниченной ответственностью Год основания: 1998 Количество сотрудников: 6",
//            address = "224003, г. Брест, Аэропорт-Брест",
//            phones = "(0162)20-38-71",
//            email = "softline@brest.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp11)
//////////////////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Софтсвисс (Softswiss)",
//            poster = "",
//description = "Софтсвисс, ООО (Softswiss) Общество с ограниченной ответственностью Год основания: 2010 Количество сотрудников: 5 УНП: 191303263",
//            address = "220005, г. Минск, пр-т Независимости, 58-104",
//            phones = "(029)750-86-23",
//            email = "order@softswiss.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp12)
//////////////////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Софттеко",
//            poster = "",
//description = "Софттеко, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 10 УНП: 191011692",
//            address = "220108, г. Минск, ул. Казинца, 88-18",
//            phones = "(029)753-19-48",
//            email = "alex.kutsko@iteco.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
//////////////////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Спесифик-Груп",
//            poster = "",
//description = "Спесифик-Груп, ИООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 30 УНП: 190726059",
//            address = "220033, г. Минск, пр-т Партизанский, 2-512",
//            phones = "(017)223-38-11, (029)651-18-03",
//            email = "serge.gimburg@specific-group.by",
//            website = "www.specific-group.by"
//        )
//        databaseParent.push().setValue(corp14)
//////////////////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СпесификГруп",
//            poster = "",
//description = "СпесификГруп, ИООО Гродненский ф-л Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 12 УНП: 190726059",
//            address = "230029, г. Гродно, ул. Горького, 49-623, 624",
//            phones = "(029)780-45-40",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
////////////////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Специальное конструкторское бюро",
//            poster = "",
//description = "Специальное конструкторское бюро Государственное предприятие Год основания: 1989 Количество сотрудников: 35 УНП: 600118742",
//            address = "222120, Минская обл., г. Борисов, ул. Нормандия-Неман, 167/2",
//            phones = "(0177)72-27-81, 70-60-32",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp16)
////////////////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СТ-Софт",
//            poster = "",
//description = "СТ-Софт, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 4 УНП: 190508031",
//            address = "220035, г. Минск, ул. Тимирязева, 65/а-607",
//            phones = "",
//            email = "info@stsoft.by",
//            website = "www.stsoft.by"
//        )
//        databaseParent.push().setValue(corp17)
////////////////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Строитель",
//            poster = "",
//description = "Строитель, программный комплекс Частное предприятие Год основания: 1992 Количество сотрудников: 1 УНП: 100484080",
//            address = "220007, г. Минск, ул. Могилевская, 34",
//            phones = "(017)228-11-43, (029)750-80-63",
//            email = "bogomolow@tut.by",
//            website = ""
//        )
//       databaseParent.push().setValue(corp18)
////////////////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Стройинтех-плюс",
//            poster = "",
//description = "Стройинтех-плюс, ПК Кооперативное предприятие Год основания: 2001 Количество сотрудников: 12 УНП: 190196311",
//            address = "220033, г. Минск, ул. Серафимовича, 11-206",
//            phones = "(017)298-35-19, 298-35-71",
//            email = "stroyp@inbox.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp19)
//////////////////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Стройсервиссофт",
//            poster = "",
//description = "Стройсервиссофт, ООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 2 УНП: 190704292",
//            address = "220053, г. Минск, ул. Будславская, 2-13",
//            phones = "(017)289-00-98",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp20)
   }
}