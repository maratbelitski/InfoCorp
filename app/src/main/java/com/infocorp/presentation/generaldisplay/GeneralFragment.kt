package com.infocorp.presentation.generaldisplay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentGeneralBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.UpdateBottomMenu

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw Exception()

    private lateinit var updateStateBottomMenu: UpdateBottomMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        updateStateBottomMenu = activity as MainActivity
        updateStateBottomMenu.enableBottomMenu()


        // проверка dadata
//        GlobalScope.launch(Dispatchers.Default) {
//           val result = CorporationFactory().corporationService.getAllCorporations("АБУШЕНКО ВИ")
//            Log.i("MyLog","${result.body()?.suggestions}")
//        }


//        val database = Firebase.database.getReference("CORPORATION")
//
//
//        val corp1 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Компит Девелопер Системс",
//            description = "Compit Developer Systems, ООО (Компит Девелопер Системс)Общество с ограниченной ответственностью. Год основания: 2001. Количество сотрудников: 20. УНП: 190270634",
//            address = "220141, г. Минск, пр-т Независимости, 168/1",
//            phones = "(017)279-34-50, (029)620-02-06",
//            email = "ksa@compit.com.by",
//            website = "www.compit-ds.com"
//        )
//        val corp2 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "КОМПИТ ТЕХНОЛОДЖИС",
//            poster = "http://belorussia.su/com_logo/1338817047logo1_big.jpg",
//            description = "Компания CompIT Technologies является Платиновым партнером и эксклюзивным на территории Республики Беларусь дистрибьютором корпорации Oracle. CompIT Technologies осуществляет продажу лицензий на программное обеспечение, поставку серверов и систем хранения информации корпорации Oracle. Компания является Сертифицированным Центром технической поддержки по продуктам Oracle на территории Республики Беларусь, Авторизованным центром обучения продуктам и технологиям Oracle, предлагает услуги профессионального консалтинга по вопросам проектирования, разработки, экспертизы и сопровождения информационных систем на базе программных продуктов Oracle, настройки существующих прикладных систем, выбору технических и программных средств.",
//            address = "пр. Победителей, 23/1, офис 322, г. Минск, Республика Беларусь, 220004",
//            phones = "+375 17 256 08 70",
//            email = "oracle@compit.by",
//            website = "http://compit.by"
//        )
//        val corp3 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "DEVTEAM",
//            poster = "http://belorussia.su/com_logo/1438325314logo1_big.jpg",
//            description = "Дизайн и разработка сайтов, поисковое продвижение сайтов, контекстная реклама, техническая поддержка сайтов, реконструкция сайтов.",
//            address = "Республика Беларусь, г. Минск, ул. Радиальная, 11а",
//            phones = "+375 17 295-79-99",
//            email = "info@devtm.by",
//            website = "http://www.devtm.by"
//        )
//        val corp4 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "ZIZOR",
//            poster = "http://belorussia.su/com_logo/1559901489logo1_big.jpg",
//            description = "Создание продающих Лендинг Пейдж в Минске с высокой конверсией. Мы создали и запустили рекламу 248 продающих Landing Page в 57 сферах бизнеса. Создадим продающий сайт, который приведет от 100 до 1000 клиентов уже в первый месяц работы всего за 14 дней.",
//            address = "220013, РБ, г. Минск, ул. Я.Коласа, д. 37",
//            phones = "+375 44 549 52 17",
//            website = "https://zizor.by"
//        )
//        val corp5 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "EKA soft",
//            poster = "http://belorussia.su/com_logo/1462517894logo1_big.jpg",
//            description = "EKA soft – частная компания, основанная в 2008 году. Основной специализацией компании являются разработка программного обеспечения, продвижение в интернете для различных ниш бизнеса, контекстная реклама. Специалистами компании выполнен не один десяток коммерческих проектов для клиентов из России, США, Канады и Белоруссии. За плечами многолетний опыт разработки программного обеспечения как для небольших компаний, так и для крупных предприятий.",
//            address = "ул. Нарочанская 11, офис 33, 220020, Республика Беларусь, г.Минск",
//            phones = "+375(44) 781 04 46",
//            email = "info@eka-soft.by",
//            website = "eka-soft.by"
//        )
//        val corp6 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Elab Media",
//            poster = "http://belorussia.su/com_logo/1391056616logo1_big.jpg",
//            description = "Если Вы талантливы и трудолюбивы, то компания ELAB MEDIA станет для Вас лучшим выбором на пути к своей мечте. Мы – современная интернет компания, компания–лидер. Мы стремимся доминировать в тех сегментах, где сфокусированы наши интересы. Своим сотрудникам мы предлагаем достойное вознаграждение и благодарности счастливых клиентов. Мы искренне любим своих клиентов и делаем их жизнь лучше, поэтому мы предъявляем к своим сотрудникам разумные требования. Залогом успеха и карьерного роста в нашей компании являются: ответственность, эффективность, порядочность и неизменное стремление решать проблемы клиентов. Наши сотрудники получают полноценное обучение и четкое руководство. Для старта не требуются «корочка» о высшем образовании и IQ выше среднего. В нашей компании Вы станете профессионалами, если приложите необходимые усилия. Со своей стороны мы окажем полную поддержку. Наша компания имеет несколько офисов в России и Беларуси и продолжает расширяться. Информация о вакансиях на нашем сайте: http://www.elab.by. Присылайте свои резюме на info@elab.by. Мы ответим на все письма.",
//            phones = "+375 (17) 205 89 28, +375 (29) 763 60 12, +375 (44) 587 57 81",
//            email = "job@elab.by",
//            website = "http://elab.by"
//        )
//        val corp7 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Electronic Digital Technology",
//            poster = "http://belorussia.su/com_logo/1370177896logo1_big.jpg",
//            description = "Разработка программного обеспечения и веб-приложени",
//            address = "г. Полоцк, ул. Радищева, 7",
//            phones = "+375298974486",
//            email = "infoedigitaltechnology@gmail.com",
//            website = "http://edt.webatu.com"
//        )
//        val corp8 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Embedded Solutions BLR",
//            poster = "http://belorussia.su/com_logo/1502794767logo1_big.jpg",
//            description = "Немецкая компания Schleissheimer GmbH открывает дочернюю компанию „Embedded Solutions BLR“  в Минске и приглашает талантливых специалистов стать частью нашей команды. Мы предлагаем широкие возможности как карьерного так и профессионального роста. Нам нужны как специалисты среднего уровня так и профессионалы высокого класса, способные решать сложные задачи и управлять командой. Наша компания уже более 25 лет разрабатывает програмное обеспечение для ведущих мировых автопроизводителей. Наше програмное обеспечение используется такимим компаниями как Daimler, VW, BMW, Renault и др. Чтобы оценить объём задач, следует учитывать, что современный автомобиль представляет собой набор мощных вычислительных устройств, взаимосвязанных между собой и обеспечивающих высокий уровень комфорта и безопасности водителя и пассажиров, а также, в перспективе, автономное перемещение транспортного средства. Помимо этого, наша компания занимается разработкой собственных продуктов для Windows и Linux (как для PC так и для встраиваемых систем различного назначения), а также разработка и реализация небольших управляющих устройств. Например наша компания ведёт разработку интерактивного графического интерфейса для промышленных машин компании M&#220;LLER Umwelttechnik GmbH & Co. KG.",
//            address = "Республика Беларусь, г.Минск, пер. Автодоровский, д. 15.корпус. 2, кабинет 215/57",
//            phones = "+375444752413",
//            email = "job@em-so.com"
//        )
//        val corp9 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "EPAM Anywhere",
//            poster = "http://belorussia.su/com_logo/1632997913logo1_big.jpg",
//            description = "EPAM Anywhere – платформа, которая позволяет IT-фрилансерам быстро находить работу, участвуя в проектах компании. Клиенты EPAM – это глобальные компании из списка Fortune Global 2000, которые работают в индустриях FinTech, Ecommerce +других. На платформе собраны вакансии для разработчиков программного обеспечения (ПО), тестировщиков ПО, проект-менеджеров, бизнес-аналитиков и других ІТ-специалистов. Все вакансии EPAM Anywhere предполагают работу на проектах на долгосрочной основе. EPAM Anywhere гарантирует минимальный простой между проектами и заблаговременно подбирает проект для специалистов.",
//            address = "220141,Минск,Купревича 1 корп 1 ком 110",
//            phones = "+375173890100",
//            email = "contact.anywhere@epam.com",
//            website = "http://anywhere.epam.com/ru"
//        )
//        val corp10 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Extrit.by - Интернет-маркетинг",
//            poster = "http://belorussia.su/com_logo/1473249183logo1_big.jpg",
//            description = "Вам нужно SEO-продвижение сайта и качественный интернет-маркетинг?! Компания «Extrit» знает об этом все! Многопрофильная команда готова приступить к работе уже сегодня! Воспользуйтесь услугами настоящих профессионалов!",
//            address = "224030, РБ, г. Брест, ул. Советских пограничников, 52 — каб. 21А",
//            phones = "+375 29 720 30 90",
//            email = "office@extrit.by",
//            website = "http://extrit.by"
//        )
//
//        database.push().setValue(corp1)
//        database.push().setValue(corp2)
//        database.push().setValue(corp3)
//        database.push().setValue(corp4)
//        database.push().setValue(corp5)
//        database.push().setValue(corp6)
//        database.push().setValue(corp7)
//        database.push().setValue(corp8)
//        database.push().setValue(corp9)
//        database.push().setValue(corp10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}