package com.infocorp.presentation.generaldisplay

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentGeneralBinding
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

//        lifecycleScope.launch {
//            delay(5000)
//            addCorps()
//        }

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
        val corp1 = CorporationDto(
            idFirebase = databaseParent.key.toString(),
            name = "TEST222",
            poster = "",
description = "Количество сотрудников: 9 УНП: 190243574",
            address = "220035, г. Минск, ул. Тарханова, 13-6а",
            phones = "(029)766-90-05",
            email = "ntv@vashsite.by",
            website = "http://vashsite.by"
        )
        databaseParent.push().setValue(corp1)
////////////////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "SmartSite",
//            poster = "http://belorussia.su/com_logo/1412768939logo1_big.jpg",
//description = "Студия веб-дизайна SmartSite предлагает услуги по созданию, продвижению и сопровождению сайтов. Сайт - наиболее удобный и мощный инструментом маркетинговой компании любого бизнес-проекта. Он является универсальным каналом продаж и коммуникации с потребителем, который работает на Вас и днем, и ночью. Готовый сайт - результат решения цепочки задач: от анализа и постановки задания до запуска проекта и его развития.",
//            address = "",
//            phones = "+375 29 740-77-36",
//            email = "mail@smartsite.by",
//            website = "http://smartsite.by"
//        )
//        databaseParent.push().setValue(corp2)
////////////////////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СТХМ",
//            poster = "http://belorussia.su/com_logo/1340713250logo1_big.jpg",
//description = "Наша компания является одной из немногих на территории Беларуси, которая занимается разработкой собственного программного продукта – международной системы электронных платежей EcoCard. В отличии от наших конкурентов, мы мыслим и действуем не по шаблону.",
//           address = "ул. Бирюзова, 10а",
//            phones = "375291778737",
//            email = "karina.nikulshina@minsk.ctxm.com",
//            website = "www.ctxm.by"
//        )
//        databaseParent.push().setValue(corp3)
////////////////////////
////////////////////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Cистемы экономического моделирования",
//            poster = "",
//description = "Технологии и системы экономического моделирования, ООО Общество с ограниченной ответственностью Год основания: 2005 Количество сотрудников: 8 УНП: 190619341",
//            address = "220013, г. Минск, ул. Кульман, 2-302",
//            phones = "",
//            email = "1cinfo@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp4)
////////////////////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Технология решений",
//            poster = "",
//description = "Технология решений, ОДО Общество с дополнительной ответственностью Год основания: 2007 Количество сотрудников: 9 УНП: 390466912",
//            address = "210015, г. Витебск, пр-т Черняховского, 33, помещение 4, оф. 5",
//            phones = "(0212)27-22-73, (029)717-09-24",
//            email = "info@tehnologia.by",
//            website = "www.tehnologia.by"
//        )
//        databaseParent.push().setValue(corp5)
////////////////////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Техносапр МАИТ",
//            poster = "",
//description = "Техносапр МАИТ, УП Частное предприятие Год основания: 1992 Количество сотрудников: 3 УНП: 100187532",
//            address = "220049, г. Минск, ул. Новгородская, 4-204",
//            phones = "(017)237-13-53",
//            email = "info@wintecs.by",
//            website = "www.wintecs.by"
//        )
//        databaseParent.push().setValue(corp6)
////////////////////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Техносервисцентр",
//            poster = "",
//description = "Техносервисцентр, СТ ООО Общество с ограниченной ответственностью Год основания: 1997 Количество сотрудников: 9 УНП: 500493701",
//            address = "230029, г. Гродно, ул. Горького, 72/2 (4 этаж)",
//            phones = "55-11-10,55-11-12",
//            email = "technosc@tut.by",
//            website = "http://www.plds.by"
//        )
//        databaseParent.push().setValue(corp7)
////////////////////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Тиком",
//            poster = "",
//            address = "220086, г. Минск, а/я 102",
//description = "Тиком, НТЧУП Частное предприятие Год основания: 1992 Количество сотрудников: 3",
//            phones = "+37529 7635707, +37529 6252482",
//            email = "ticom@nsys.by",
//            website = "http://chipstar.by"
//        )
//        databaseParent.push().setValue(corp8)
////////////////////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ТКП-Софт",
//            poster = "",
//description = "ТКП-Софт, ООО Общество с ограниченной ответственностью Год основания: 2004 Количество сотрудников: 15",
//            address = "пр. Машерова, 78, 4-й этаж",
//            phones = "",
//            email = "info@tcp-soft.com",
//            website = "www.tcp-soft.com"
//        )
//        databaseParent.push().setValue(corp9)
//////////////////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Тороид",
//            poster = "",
//description = "Тороид, ОДО Общество с дополнительной ответственностью Год основания: 2009 Количество сотрудников: 6 УНП: 290503965",
//            address = "224013, г. Брест, ул. Кирова, 161",
//            phones = "(0162)97-40-40, (029)721-14-89",
//            email = "toroid@mail.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
////////////////////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Точка выбора",
//            poster = "",
//description = "Кадровое агентство, ищем сотрудников для международной компании",
//            address = "",
//            phones = "+7 (495) 974-22-47",
//            email = "ceb@tvbr.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp11)
////////////////////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Транстэкс-НТ",
//            poster = "",
//description = "Транстэкс-НТ, ООООбщество с ограниченной ответственностью Год основания: 2000 Количество сотрудников: 21 УНП: 100940705",
//            address = "220034, г. Минск, ул. Чапаева, 5-209",
//            phones = "(017)294-15-92",
//            email = "transt@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp12)
////////////////////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Турникет инфо",
//            poster = "",
//description = "Турникет инфо, ООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 10 УНП: 191159826",
//            address = "220014, г. Минск, ул. Малая, 1, помещение 1, оф. 1",
//            phones = "(017)223-78-07",
//            email = "mail@turniket.info",
//            website = "www.turniket.info"
//        )
//        databaseParent.push().setValue(corp13)
////////////////////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Ультрасофт",
//            poster = "",
//description = "Ультрасофт, ОДО Общество с дополнительной ответственностью Год основания: 1997 Количество сотрудников: 7 УНП: 101435733",
//            address = "220090, г. Минск, Логойский тракт, 20-415г",
//            phones = "(017)281-42-85, 283-99-55",
//            email = "recepson@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp14)
////////////////////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Вычислительный центр БГУИР",
//            poster = "",
//description = "Учебно-вычислительный центр БГУИР, обособленное структурное подразделение Государственное предприятие Год основания: 2003 Количество сотрудников: 25",
//            address = "220027, г. Минск, ул. Платонова, 39",
//            phones = "",
//            email = "pna@bsuir.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
//////////////////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Учетные системы",
//            poster = "",
//description = "Учетные системы, ООО Общество с ограниченной ответственностью Год основания: 2010 Количество сотрудников: 3 УНП: 691281287",
//            address = "222120, Минская обл., г. Борисов, пр-т Революции, 4-60",
//            phones = "(044)756-32-87",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp16)
//////////////////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Фарисей",
//            poster = "",
//description = "Фарисей, ОДО Общество с дополнительной ответственностью Год основания: 2006 Количество сотрудников: 15 УНП: 490496649",
//            address = "246001, г. Гомель, ул. Заслонова, 1-5",
//            phones = "(0232)44-10-56, (029)349-97-67",
//            email = "farisey@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp17)
//////////////////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Фоикс",
//            poster = "",
//description = "Фоикс, ООО Общество с ограниченной ответственностью Год основания: 1991 Количество сотрудников: 8 УНП: 100145757",
//            address = "220005, г. Минск, ул. Красная, 17",
//            phones = "(017)284-77-85, 288-10-76",
//            email = "lis@foix.minsk.by",
//            website = "www.minskfoix.com"
//        )
//       databaseParent.push().setValue(corp18)
//////////////////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Форте",
//            poster = "",
//description = "Форте, ИП Иностранное предприятие Год основания: 2011 Количество сотрудников: 20",
//            address = "220113, г. Минск, ул. Мележа, 1-1101",
//            phones = "(025)617-45-98",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp19)
////////////////////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Фотопоинт",
//            poster = "",
//description = "Фотопоинт, ООО Общество с ограниченной ответственностью Год основания: 2003 Количество сотрудников: 6",
//            address = "213826, Могилевская обл., г. Бобруйск, ул. К. Маркса, 22/20-77",
//            phones = "(0225)58-55-66",
//            email = "contact@bobr.by",
//            website = "www.bobr.by"
//        )
//        databaseParent.push().setValue(corp20)
   }
}