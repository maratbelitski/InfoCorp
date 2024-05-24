package com.infocorp.presentation.generaldisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentGeneralBinding
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null

    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw NullPointerException()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

    private val fragmentViewModel: GeneralFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralBinding.inflate(layoutInflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        onObservers()


        val databaseParent = Firebase.database.getReference("CORPORATION")
//       пользовательская бд
        val databaseChild = Firebase.database.getReference("USER_CORPORATION")

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
//            name = "Инструментальные технологии",
//            poster = "",
//description = "Инструментальные технологии, ЗАО Закрытое акционерное общество Год основания: 1997 Количество сотрудников: 21 УНП: 101269853",
//            address = "220141, г. Минск, ул. Купревича, 1/3-404",
//            phones = "(017)265-91-51",
//            email = "intech@belsonet.net",
//            website = "www.intech.belsonet.net"
//        )
//        databaseParent.push().setValue(corp1)
////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Интеграционные технологии",
//            poster = "http://belorussia.su/com_logo/1406723327logo1_big.jpg",
//description = "Интеграционные технологии (ИНТЕК) – компания - системный интегратор, занимающаяся созданием и внедрением автоматизированных информационных систем для организаций, ориентированных на управление складскими территориями, логистикой и торговлей. ИНТЕК как юридическое лицо образовался в 2014 года в городе Минске как совместная инициатива ключевых ИТ специалистов крупной компании дистрибьютора, создать высокопрофессиональную команду способную ставить перед собой высокие и амбициозные цели и эффективно достигать их. Отличительной чертой нашего подхода к делу перед конкурентами является высокая степень автоматизации бизнес-процессов и проектный подход с использованием передовых проектных технологий. Наша компания также выделяется наличием большого числа профессионалов в штате, которые имеют богатый опыт внедрения, разработки и управления проектами в самых крупных торговых и логистических компаниях Беларуси. Наша команда на данный момент уже разработала решение по управлению дистрибуцией для Беларуси на базе типовой конфигурации Управление торговлей 11 компании 1C. Также нами ведутся работы по адаптации и доработке принципиально нового по масштабу решения по управлению предприятием ERP 2.0.",
//            address = "220007, г. Минск, ул. Могилевская, д. 18, пом. 4Н",
//            phones = "+37517 219 00 85",
//            email = "info@intec.by",
//            website = "http://www.intec.by"
//        )
//        databaseParent.push().setValue(corp2)
//////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Интелико Системс",
//            poster = "http://belorussia.su/com_logo/1338807548logo1_big.jpg",
//description = "Дизайн. Разработка интернет-ориентированного программного обеспечения.",
//            address = "ул. Филимонова 53-715",
//            phones = "+375172118046",
//            email = "connect@intelico.su",
//            website = "http://www.intelico.su"
//        )
//        databaseParent.push().setValue(corp3)
//////////
//////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Интеллектсофт",
//            poster = "",
//description = "Интеллектсофт, ООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 50 УНП: 190890707",
//            address = "г. Минск, пр. Держинского, 57, 14-1",
//            phones = "",
//            email = "hr@intellectsoft.net",
//            website = "www.intellectsoft.net"
//        )
//        databaseParent.push().setValue(corp4)
//////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Интермех",
//            poster = "",
//description = "Интермех, ОДО Общество с дополнительной ответственностью Год основания: 1990 Количество сотрудников: 35 УНП: 100176519",
//            address = "220004, г. Минск, ул. Короля, 51 (3 этаж)",
//            phones = "(017)306-21-30",
//            email = "cad@intermech.ru",
//            website = "www.intermech.ru"
//        )
//        databaseParent.push().setValue(corp5)
//////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИнтернетСоздатели",
//            poster = "http://belorussia.su/com_logo/1592385670logo1_big.jpg",
//description = "Профессионально занимаемся разработкой и продвижение сайтов уже 11 лет. Можем помочь решить данную задачу. Работаем официально, по договору. Гарантия 12 мес. Всегда на связи! +375297139003 Viber, WhatsApp.",
//            address = "ул. Советская, д. 68, офис 402",
//            phones = "+375 (29) 743-19-05",
//            email = "internetsozdateli1@gmail.com",
//            website = "https://internetsozdateli.by"
//        )
//        databaseParent.push().setValue(corp6)
//////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инфо-Сервис",
//            poster = "http://belorussia.su/com_logo/1362811456logo1_big.jpg",
//description = "Наша компания оказывает комплекс услуг, связанных с сбором и обработкой информации, а также разрабатывает программное обеспечение, необходимое в ходе деятельности организаций, таких риэлторские, юридические и бухгалтерские компании. Специалисты компании грамотно и оперативно проводят работы по всем важнейшим направлениям. Информационные услуги занимают прочное место в сфере нашей работы. Мы обеспечиваем информационную и физическую защиту носителей. Сотрудничество с нами гарантирует высокий уровень безопасности.",
//            address = "Ул. Сурганова, 28",
//            phones = "8-929-698-09-61",
//            email = "it-service-company@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
//////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инфокард",
//            poster = "",
//description = "Инфокард, ООООбщество с ограниченной ответственностью Год основания: 2000Количество сотрудников: 10УНП: 190378061",
//            address = "220114, г. Минск, ул. Буйницкого, 21-21",
//            phones = "(017)267-56-62",
//            email = "info@infocard.by",
//            website = "www.infocard.by"
//        )
//        databaseParent.push().setValue(corp8)
//////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИВЦ Бел.ж.д",
//            poster = "",
//description = "Информационно-вычислительный центр УП «Витебское отделение Белорусской железной дороги» осуществляет следующие виды работ: техническое обслуживание, ремонт ПЭВМ и офисной техники; заправка картриджей для лазерных принтеров и МФУ; строительство и обслуживание локальных сетей; настройка и техническое обслуживание серверов и сетевого оборудования; выполнение работ по разработке программного обеспечения; оказание услуг по сопровождению программных продуктов.",
//            address = "",
//            phones = "+37529716-62-81",
//            email = "ivc.vitebsk@gmail.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИВЦ авиации",
//            poster = "",
//description = "Информационно-вычислительный центр авиации, РУП Государственное предприятие Год основания: 1979 Количество сотрудников: 17 УНП: 100035790",
//            address = "220039, г. Минск, ул. Короткевича, 7",
//            phones = "(017)222-54-17, 224-52-73",
//            email = "ivc@ivcavia.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
////////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Информационные компьютерные системы",
//            poster = "",
//description = "Информационные компьютерные системы, ООО Общество с ограниченной ответственностью Год основания: 1992 Количество сотрудников: 5УНП: 200082067",
//            address = "224005, г. Брест, ул. Комсомольская, 40-304",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp11)
////////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Информация",
//            poster = "",
//description = "Информация, НПП Частное предприятие Год основания: 1997 Количество сотрудников: 5",
//            address = "220075, г. Минск, ул. Ротмистрова, 62-192",
//            phones = "(017)344-18-15",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp12)
////////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инфостар",
//            poster = "",
//description = "Инфостар, СООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 20УНП: 190682990",
//            address = "220113, г. Минск, ул. Мележа, 5/2-211",
//            phones = "(017)280-98-70",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
////////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инфотех",
//            poster = "",
//description = "Инфотех, УП Частное предприятие Год основания: 1991 Количество сотрудников: 15УНП: 100067268",
//            address = "220073, г. Минск, пер. 4-й Загородный, 56/а",
//            phones = "(017)228-25-57",
//            email = "viktor_f@tut.by",
//            website = "www.infotech.by"
//        )
//        databaseParent.push().setValue(corp14)
////////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инфотехсервис",
//            poster = "",
//description = "Инфотехсервис, УП Государственное предприятие Год основания: 1991 Количество сотрудников: 22 УНП: 500036830",
//            address = "230010, г. Гродно, ул. Гая, 2 (9 этаж)",
//            phones = "(0152)54-69-63, 54-94-58",
//            email = "tex_info@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
//////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инфотехсофтбел",
//            poster = "",
//description = "Инфотехсофтбел, ЧП Частное предприятие Год основания: 2008 Количество сотрудников: 7 УНП: 190853313",
//            address = "220015, г. Минск, ул. Пономаренко, 35/а-726",
//            phones = "po@infotech.by",
//            email = "po@infotech.by",
//            website = "http://infotech.by"
//        )
//        databaseParent.push().setValue(corp16)
//////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "СИБ софтвэр",
//            poster = "http://belorussia.su/com_logo/1340871107logo1_big.jpg",
//description = "СИБ софтвэр ГмбХ работает на рынке информационных технологий Германии уже более 20 лет. Компания разрабатывает DMS- и DLMS -системы, предназначенные для банковского и страхового сектора, а также для сектора госуправления. Белорусское представительство было открыто в 2007 году, а в 2009 преобразовано в ИООО  \"СИБ софтвэр\" . В мире используются более 2-х миллионов лицензий на продукты, разработанные ИООО  \"СИБ софтвэр\" совместно с СИБ софтвэр ГмбХ. Ключ к успеху компании – это ее сотрудники.  СИБ является призером конкурса «Лучшие ИТ-компании Беларуси глазами сотрудников 2012». Для каждого нового члена команды организуется образовательная поездка в Германию. В обширный соцпакет помимо прочего входит медицинское страхование. Цель компании: «Долгосрочное сотрудничество, основанное на доверии».",
//            address = "Ул. Левкова, 24-7, Минск 220007",
//            phones = "+375 17 298 59 03",
//            email = "info@cib.by",
//            website = "http://www.cib.by"
//        )
//        databaseParent.push().setValue(corp17)
//////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Головкин А.А.",
//            poster = "http://belorussia.su/com_logo/1413283195logo1_big.jpg",
//description = "ДИЗАЙН Полностью соответствующий Вашим требованиям и представлениям о вкусе. СРОКИ 10 - 21 календарных дней. РЕЗУЛЬТАТ Профессионально сделанный интернет-ресурс, повышающий вес Вашего бизнеса. От 100 посетителей в месяц, целенаправленно посещающих Ваш сайт, каждый из которых может стать Вашим клиентом. УНП 591161373, ИП Головкин Алексей Александрович",
//            address = "230006, г.Гродно, м-н Фолюш, д.15/191, кв.13",
//            phones = "+375298825577",
//            email = "info@nextlevel.com",
//            website = "http://nextlevel.by"
//        )
//       databaseParent.push().setValue(corp18)
//////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИП Костюк",
//            poster = "http://belorussia.su/com_logo/1623856634logo1_big.jpg",
//description = "Лечение сайта от вирусов за 24 часа. Оказываем полный комплекс услуг в области информационной безопасности сайтов. Оперативная помощь в очистке сайта от вирусов, диагностики сайта на предмет взлома и заражения, восстановление сайта после хакерского взлома. Вылечить сайт Мы разрабатали новые методики обнаружения вирусов, экстренного лечения, удаление вирусов с сайта, обнаружения уязвимостей CMS и восстановления работоспособности сайта",
//            address = "Минск, ул.Сырокомли 38 офис 22",
//            phones = "+375 (29) 934-43-08",
//            email = "info@webster.by",
//            website = "https://webster.by"
//        )
//        databaseParent.push().setValue(corp19)
////////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИП Лиcтpaтeнko",
//            poster = "http://belorussia.su/com_logo/1559235263logo1_big.jpg",
//description = "Разработка программного обеспечения для индивидуальных предпринимателей и малых фирм.",
//            address = "220141, г. Минск, ул. Купревича, 1/3-404",
//            phones = "+375 29 5612441",
//            email = "",
//            website = "http://ip-soft.by"
//        )
//        databaseParent.push().setValue(corp20)






   }



    private fun onObservers() {
        lifecycleScope.launch {
            fragmentViewModel.showShimmer
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        when (it) {
                            true -> {
                                shimmerLayout.shimmer.visibility = View.VISIBLE
                                statisticCard.statisticCardForView.visibility = View.GONE
                            }

                            false -> {
                                statisticCard.statisticCardForView.visibility = View.VISIBLE
                                shimmerLayout.shimmer.visibility = View.GONE
                            }
                        }
                    }
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.allCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.statisticCard.countAllInBase.text = it.toString()
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
        updateStateBottomMenu.enableBottomMenu()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}