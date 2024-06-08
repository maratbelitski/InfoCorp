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
//            name = "КОМПИТ ЭКСПЕРТ",
//            poster = "",
//            address = "220017 г. Минск,ул. Притыцкого, 156, 9 этаж, помещение 14",
//description = "ООО «КОМПИТ ЭКСПЕРТ» работает на рынке информационных технологий Республики Беларусь более 27 лет и имеет большой опыт реализации комплексных проектов по автоматизации бизнес-процессов, разработке, внедрению, технической поддержке и сопровождению информационных систем различной степени сложности, является центром компетенций по технологическому стеку работы с данными и их использованию. Мы создаем надежный фундамент для развития и процветания наших заказчиков и партнеров, внедряя новейшие высокотехнологичные и эффективные решения, которые отвечают целям и задачам современного бизнеса. НАПРАВЛЕНИЯ БИЗНЕСА: Работа с большими данными и системами аналитики. Цифровая трансформация бизнес-процессов. Консалтинг и аудит. Техническая поддержка и сопровождение",
//            phones = "+37517 370 86 60",
//            email = "info@compit.by",
//            website = "http://compit.by"
//        )
//        databaseParent.push().setValue(corp1)
////////////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Тамонтэн-групп",
//            poster = "http://belorussia.su/com_logo/1427270274logo1_big.jpg",
//description = "Установка, настройка ,доработка, обслуживание и сопровождение программ на платформе 1С",
//            address = "пр.Независимости 85В, оф.81А",
//            phones = "+375 (29) 394-44-36",
//            email = "upr@sages.by",
//            website = "http://sages.by"
//        )
//        databaseParent.push().setValue(corp2)
////////////////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ООО ВПН Бай",
//            poster = "http://belorussia.su/com_logo/1367991372logo1_big.jpg",
//description = "VPN.BY - белорусский информационно-коммуникационный портал. Компания разрабатывает и распространяет программные решения ВПН Бай и сопутствующее ПО, занимается разработкой, seo-продвижением и сопровождением веб-сайтов, веб-хостингом, техподдержкой и сервисным обслуживанием программного обеспечения и компьютерного оборудования клиентов-заказчиков. Портал предлагает последние новости со всего мира, электронную почту, объявления и блоги, общение в чате, видеоконференции, форум и социальная сеть. Для скачивания и обмена файлами используйте хостинги файлов, видео и фото, виртуальную библиотеку, торрент-трекер, DC хаб. Для раскрутки и продвижения Ваших сайтов, получения от них дохода - сервис контекстной рекламы, синхронизировать время компьютера или же Вы сможете стать профессиональным радиодиджеем и порадовать Ваших друзей. Для игроков - мониторинг игровых серверов.",
//            address = "222720, Беларусь, Минская обл., г. Дзержинск, ул. Фоминых, 56, к. 30",
//            phones = "+375292519596",
//            email = "contacts@vpn.by",
//            website = "http://vpn.by"
//        )
//        databaseParent.push().setValue(corp3)
////////////////////
////////////////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Котвек",
//            poster = "http://belorussia.su/com_logo/1700220825logo1_big.jpg",
//description = "Ищете ИТ аутсорсинг? Мы оказываем услуги мобильной и веб разработки, управления данными, мониторинга 1850 успешных кейсов Талантливая и опытная команда.",
//            address = "Республика Беларусь, 220034, г. Минск, ул. Чапаева 4, 2 этаж",
//            phones = "+375(29) 686-05-90",
//            email = "info@cotvec.com",
//            website = "https://cotvec.com"
//        )
//        databaseParent.push().setValue(corp4)
////////////////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Скор Системс",
//            poster = "http://belorussia.su/com_logo/1648469415logo1_big.jpg",
//description = "InSales.by – профессиональная платформа для создания интернет-магазинов и управления онлайн продажами, на которой работает уже более 11000 успешных интернет-магазинов. InSales.by позволяет не только решать онлайн задачи, которые стоят перед любым интернет-магазином, но и комплексно управлять бизнес-процессами: управление собственными курьерами, автоматизация точек продаж и самовывоза, мобильное приложение для управления магазином. Специализация на сфере eCommerce позволяет inSales всегда решать актуальные задачи владельцев интернет-магазинов за счет мощнейшего inSales AppStore (более 300 интегрированных сервисов для интернет-магазинов) и плотного взаимодействия с лидирующими сервисами Байнета. А поддержка в формате 24/7 позволяет оперативно решить не только технические вопросы, но и вопросы по маркетингу и бизнесу в целом. С момента основания в 2008 году InSales предоставляет свои услуги по модели SaaS (ПО как услуга). Также компания оказывает дополнительные услуги для владельцев интернет-магазинов: разработка дизайна, продвижение интернет-магазинов и проведение обучения основам интернет торговли и маркетинга.",
//            address = "220019, Республика Беларусь г. Минск, ул. Лобанка, д. 79",
//            phones = "+375 (29) 380-22-21",
//            email = "hello@insales.by",
//            website = "https://insales.by"
//        )
//        databaseParent.push().setValue(corp5)
////////////////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Ориентсофт-ПиР",
//            poster = "",
//description = "Ориентсофт-ПиР, ЗАОЗакрытое акционерное обществоГод основания: 1994Количество сотрудников: 25УНП: 190378087",
//            address = "220036, г. Минск, ул. Р. Люксембург, 95-520",
//            phones = "(017)286-16-75",
//            email = "sales@orientsoft.by",
//            website = "www.orientsoft.by"
//        )
//        databaseParent.push().setValue(corp6)
////////////////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Орсегия",
//            poster = "",
//description = "Орсегия, УП Частное предприятие Год основания: 2000 Количество сотрудников: 5 УНП: 101327726",
//            address = "220040, г. Минск, ул. Восточная, 33-4",
//            phones = "(017)287-67-31-секретарь",
//            email = "orsegie@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
////////////////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Открытые технологии",
//            poster = "",
//description = "Открытые технологии, ИООО Иностранное предприятие Год основания: 2002 Количество сотрудников: 20 УНП: 600176870",
//            address = "220030, г. Минск, ул. Белорусская, 15-11",
//            phones = "(017)266-21-31",
//            email = "minsk@ot.ru",
//            website = "www.ot.by"
//        )
//        databaseParent.push().setValue(corp8)
////////////////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Офиспарк",
//            poster = "",
//description = "Офиспарк, ООО Общество с ограниченной ответственностью Год основания: 2001 Количество сотрудников: 7 УНП: 190247327",
//            address = "220030, г. Минск, ул. Жилуновича, 15-508",
//            phones = "(017)346-84-04",
//            email = "officepark@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
//////////////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Парк высоких технологий",
//            poster = "",
//description = "Парк высоких технологий, администрация ГУ Государственное предприятие Год основания: 2006 Количество сотрудников: 32 УНП: 190690484",
//            address = "220141, г. Минск, ул. Академика Купревича, 1/1, 9 этаж",
//            phones = "(017)268-69-11, 268-69-16",
//            email = "info@park.by",
//            website = "www.park.by"
//        )
//        databaseParent.push().setValue(corp10)
////////////////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Пи-консалт бай",
//            poster = "",
//description = "Пи-консалт бай, ООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 10 УНП: 190732680",
//            address = "220013, г. Минск, ул. Кульман, 1/3-25а",
//            phones = "(017)209-84-23",
//            email = "info@pi-consult.by",
//            website = "www.pi-consult.by"
//        )
//        databaseParent.push().setValue(corp11)
////////////////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ПКС Электронный чек",
//            poster = "http://belorussia.su/com_logo/1655493419logo1_big.jpg",
//description = "Программная касса «Электронный чек» (Онлайн-касса) - специализированное программное обеспечение, предназначенное для выполнения кассовых операций при продаже товаров, выполнении работ, оказании услуг. Программная касса размещена в центре обработки данных (ЦОД,  «в облаке»)",
//            address = "220036, Минск, пр-т Жукова, 29",
//            phones = "+375 29 644 72 20",
//            email = "ira.demidovetsz@mail.ru",
//            website = "https://www.4ek.by"
//        )
//        databaseParent.push().setValue(corp12)
////////////////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ПМиС-Софпродукт",
//            poster = "",
//description = "ПМиС-Софпродукт, ОО ООбщество с ограниченной ответственностью Год основания: 2001 Количество сотрудников: 35 УНП: 800006648",
//            address = "220016, г. Минск, ул. Бобруйская, 9-309",
//            phones = "(017)209-54-01",
//            email = "info@pms-softvare.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
////////////////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Полесьеинформатика",
//            poster = "",
//description = "Полесьеинформатика, ЧУП Частное предприятие Год основания: 2006 Количество сотрудников: 15 УНП: 290449462",
//            address = "224016, г. Брест, ул. Гоголя, 75-303",
//            phones = "(0162)23-31-17",
//            email = "brest@pti.by",
//            website = "www.pti.by"
//        )
//        databaseParent.push().setValue(corp14)
////////////////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Полесьеинформатика-Гомель",
//            poster = "http://belorussia.su/com_logo/1493732199logo1_big.jpg",
//description = "Полесьеинформатика-Гомель, ЧУП Частное предприятие Год основания: 2007 Количество сотрудников: 7 УНП: 490558359",
//            address = "246028, г. Гомель, ул. Советская, 126",
//            phones = "",
//            email = "gomel@pti.by",
//            website = "www.pti.by"
//        )
//        databaseParent.push().setValue(corp15)
//////////////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Полесьеинформатика-Пинск",
//            poster = "",
//description = "Полесьеинформатика-Пинск, ЧУП Частное предприятиеГод основания: 2008 Количество сотрудников: 12 УНП: 290481670",
//            address = "225710, Брестская обл., г. Пинск, ул. Иркутско-Пинской дивизии, 36",
//            phones = "(0165)32-43-50",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp16)
//////////////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Полесьетехинформатика",
//            poster = "",
//description = "Полесьетехинформатика, ОАО Открытое акционерное общество Год основания: 1970 Количество сотрудников: 3",
//            address = "224016, г. Брест, ул. Гоголя, 75-303",
//            phones = "(0162)21-55-86, 21-39-77",
//            email = "brest@pti.by",
//            website = "www.pti.by"
//        )
//        databaseParent.push().setValue(corp17)
//////////////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Полесьетехинформатика",
//            poster = "",
//description = "Полесьетехинформатика, ОАО ИТЦ Открытое акционерное общество Год основания: 1973 Количество сотрудников: 70 УНП: 200287363",
//            address = "225710, Брестская обл., г. Пинск, ул. Иркутско-Пинской Дивизии, 36",
//            phones = "(0165)32-37-48, 32-42-21, 32-43-26,  32-34-85",
//            email = "pinsk@pti.by",
//            website = "www.pti.by"
//        )
//       databaseParent.push().setValue(corp18)
//////////////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Левер Экс Интернейшнал",
//            poster = "",
//description = "Представительство Левер Экс Интернейшнал в РБ, ИООО Иностранное предприятие Год основания: 2004 Количество сотрудников: 36 УНП: 190652691",
//            address = "220068, г. Минск, ул. Карастояновой, 32, помещение 5-1 (4 этаж)",
//            phones = "(017)290-13-20",
//            email = "info@leverx.com",
//            website = "http://leverx.com"
//        )
//        databaseParent.push().setValue(corp19)
////////////////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Прикладные системы",
//            poster = "",
//description = "Прикладные системы, ООО Общество с ограниченной ответственностью Год основания: 1997 Количество сотрудников: 51-200 УНП: 101243864",
//            address = "220055, г. Минск, ул. Каменногорская, 47, 5 этаж",
//            phones = "(017)227-10-30, (017)227-10-31",
//            email = "info@appsys.net",
//            website = "www.appsys.net"
//        )
//        databaseParent.push().setValue(corp20)
   }
}