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
//            name = "ИП Левин В.Г.",
//            poster = "",
//description = "Левин В. Г., ИП Предприниматель Количество сотрудников: 1",
//            address = "220094, г. Минск, ул. Горовца, 26-3",
//            phones = "(017)248-37-35-дом.тел., (029)648-37-35",
//            email = "autosoft@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp1)
////////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Ловата груп",
//            poster = "",
//description = "Ловата груп, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 7 УНП: 190972770",
//            address = "220024, г. Минск, ул. Мележа, 1-1424",
//            phones = "(017)268-43-91",
//            email = "info@lovata.com",
//            website = "www.lovata.com"
//        )
//        databaseParent.push().setValue(corp2)
////////////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Лоджик Вэй",
//            poster = "",
//description = "Лоджик Вэй, ООО Общество с ограниченной ответственностью Год основания: 2005 Количество сотрудников: 40 УНП: 190624658",
//            address = "220034, г. Минск, ул. Чапаева, 5-126",
//            phones = "",
//            email = "info@logic-way.com",
//            website = "http://logic-way.com"
//        )
//        databaseParent.push().setValue(corp3)
////////////////
////////////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Лоранж-2",
//            poster = "",
//description = "Лоранж-2, ОДО Общество с дополнительной ответственностью Год основания: 2006 Количество сотрудников: 10 УНП: 100792507",
//            address = "220040, г. Минск, ул. М. Богдановича, 118-324",
//            phones = "(017)293-37-48",
//            email = "lorang2@yandex.ru",
//            website = "http://ohranatruda.info"
//        )
//        databaseParent.push().setValue(corp4)
////////////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ЛюксСофт НТ",
//            poster = "",
//description = "ЛюксСофт НТ, ООО Общество с ограниченной ответственностью Год основания: 1997 Количество сотрудников: 27 УНП: 101262078",
//            address = "220125, г. Минск, пр-т Независимости, 185-49 (5 подъезд)",
//            phones = "(017)211-85-43, 211-86-78",
//            email = "luxsoft@mail.ru",
//            website = "www.luxsoft.by"
//        )
//        databaseParent.push().setValue(corp5)
////////////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ЛюксСофтплюс",
//            poster = "",
//description = "ЛюксСофтплюс, ООО Общество с ограниченной ответственностью Год основания: 1997 Количество сотрудников: 20 УНП: 600474236",
//            address = "220121, г. Минск, ул. Притыцкого, 60/1-221",
//            phones = "",
//            email = "luxsoftplus@tut.by",
//            website = "http://luxsoftplus.virtualave.net"
//        )
//        databaseParent.push().setValue(corp6)
////////////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Люцерна",
//            poster = "",
//description = "Люцерна, ОДО Общество с дополнительной ответственностью Год основания: 2002 Количество сотрудников: 3 УНП: 190403685",
//            address = "220092, г. Минск, пр-т Пушкина, 39-304",
//            phones = "",
//            email = "nasimov@visitby.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
////////////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Майфокс",
//            poster = "http://belorussia.su/com_logo/1608078182logo1_big.jpg",
//description = "Разработка и продвижение коммерческих сайтов.",
//            address = "г. Минск, ул. Кульман 21 Б, офис 115",
//            phones = "+375 (29) 657 0004",
//            email = "epsilon.digital.minsk@gmail.com",
//            website = "https://epsilon.by"
//        )
//        databaseParent.push().setValue(corp8)
////////////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Манго-групп",
//            poster = "",
//description = "Манго-групп, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 10",
//            address = "220000, г. Минск, ул. Тростенецкая, 3-501в",
//            phones = "(029)354-58-37",
//            email = "www.mango.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
//////////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "МАПСофт",
//            poster = "",
//description = "МАПСофт, ЗАО Закрытое акционерное общество Год основания: 2002 Количество сотрудников: 70 УНП: 690023265",
//            address = "220043, г. Минск, пр-т Независимости, 95/1-303",
//            phones = "",
//            email = "marketing@mapsoft.by",
//            website = "http://mapsoft.by"
//        )
//        databaseParent.push().setValue(corp10)
////////////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Марко",
//            poster = "",
//description = "Марко, ООО Общество с ограниченной ответственностью Год основания: 1998 Количество сотрудников: 100 УНП: 200339518",
//            address = "220014, г. Минск, а/я 4",
//            phones = "(017)289-90-54, 207-83-82",
//            email = "info@marco.by",
//            website = "www.marco.by"
//        )
//        databaseParent.push().setValue(corp11)
////////////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "МАТРИЦА",
//            poster = "http://belorussia.su/com_logo/1594114668logo1_big.jpg",
//description = "ООО «МАТРИЦА» - экспертная автоматизация животноводства. Мы разрабатываем и внедряем программы для животноводства. Обеспечиваем интеграцию с ГИС Меркурий. Автоматизируем учет в зоопарках. Автоматизируем свиноводство, крупный рогатый скот, кролиководство, овцеводство. Работаем с 2003 года. Более 500 клиентов. 100% сертифицированные продукты.",
//            address = "Белгород, ул. Королёва, д. 2а, корпус 2",
//            phones = "",
//            email = "expert@matrix24.ru",
//            website = "http://www.matrix24.ru"
//        )
//        databaseParent.push().setValue(corp12)
////////////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Миелофон",
//            poster = "http://belorussia.su/com_logo/1626872025logo1_big.jpg",
//description = "креативная веб студия Миелофон Разработка сайтов под ключ. Создание. Веб Дизайн. Доработка. Seo продвижение. Современная mobile-first вёрстка, быстрая загрузка, поисковая оптимизация, пожизненные консультации. Нам доверяют, нас рекомендуют! Заказать сайт легко!",
//            address = "220073, Беларусь, г. Минск, ул. Ольшевского, д. 24, пом. 7, каб. 16-71",
//            phones = "",
//            email = "",
//            website = "https://myelophone.com"
//        )
//        databaseParent.push().setValue(corp13)
////////////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Микро Экспресс",
//            poster = "",
//description = "Микро Экспресс, СООО (Micro Express) Общество с ограниченной ответственностью Год основания: 1993 Количество сотрудников: 15 УНП: 100349128",
//            address = "220002, г. Минск, ул. Кропоткина, 89-85",
//            phones = "(017)334-71-23",
//            email = "info@microexp.com",
//            website = "www.microexp.ru"
//        )
//        databaseParent.push().setValue(corp14)
////////////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Микст",
//            poster = "",
//description = "Микст, УП Частное предприятие Год основания: 2000 Количество сотрудников: 11 УНП: 100045126",
//            address = "220013, г. Минск, ул. П. Бровки, 18-309",
//            phones = "(017)292-03-81, 290-02-24",
//            email = "a.smych@mikst.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
//////////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "МиСофт НВП",
//            poster = "http://belorussia.su/com_logo/1339412446logo1_big.jpg",
//description = "ЗАО «Мисофт НВП»: является официальным партнером «1С» в Республике Беларусь и уполномоченным на продажу правообладателем. Cпециализируется на поставке, установке, обучении, внедрении и сопровождении автоматизированных систем на основе платформы 1С. ЗАО «МиСофт НВП» имеет большой опыт автоматизации предприятий различного профиля. Разрабатываемое программное обеспечение для предприятий РБ функционально соответствует законодательству РБ и нормативным актам, предоставляемым Заказчиками.",
//            address = "220125 г. Минск, ул. Шафарнянская, 11 оф. 325",
//            phones = "+375 17 286-35-76",
//            email = "info@misoft.by",
//            website = "http://misoft.by"
//        )
//        databaseParent.push().setValue(corp16)
//////////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Мобилетаг",
//            poster = "",
//description = "Мобилетаг, ИООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 29 УНП: 191049594",
//            address = "220030, г. Минск, ул. Я. Купалы, 25-302",
//            phones = "(017)328-64-56",
//            email = "snbelyavskaya@mobiletag.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp17)
//////////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Мобильные решения",
//            poster = "",
//description = "Мобильные решения, ОДО Общество с дополнительной ответственностью Год основания: 2005 Количество сотрудников: 5",
//            address = "220030, г. Минск, пл. Свободы, 23-127",
//            phones = "(017)226-14-94",
//            email = "vk@tut.by",
//            website = ""
//        )
//       databaseParent.push().setValue(corp18)
//////////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Мобити",
//            poster = "",
//description = "Мобити, ИООО Общество с ограниченной ответственностью Год основания: 2009 Количество сотрудников: 10 УНП: 191114232",
//            address = "220030, г. Минск, ул. Я. Купалы, 25-306",
//            phones = "(017)328-57-19",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp19)
////////////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Модем",
//            poster = "",
//description = "Модем, НВП ООО Общество с ограниченной ответственностью Год основания: 1991 Количество сотрудников: 43 УНП: 400087921",
//            address = "246004, г. Гомель, пр-т Космонавтов, 15-18",
//            phones = "",
//            email = "office@modem.by",
//            website = "www.modem.by"
//        )
//        databaseParent.push().setValue(corp20)
    }
}