package com.infocorp.presentation.generaldisplay

import android.content.Context
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

//    private val updateStateBottomMenu by lazy {
//        activity as MainActivity
//    }

    private val fragmentViewModel: GeneralFragmentViewModel by viewModels()

    lateinit var isNetworkAvailable: ()->Boolean
    lateinit var updateStateBottomMenu: ()->Unit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) isNetworkAvailable ={context.isNetworkAvailable()}
        if (context is MainActivity) updateStateBottomMenu ={context.enableBottomMenu()}
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralBinding.inflate(layoutInflater)
        isNetworkAvailable.invoke()
        updateStateBottomMenu.invoke()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
//            name = "ИП Менченя",
//            poster = "http://belorussia.su/com_logo/1507299668logo1_big.jpg",
//description = "В наших проектах профессиональный дизайн сочетается со сложными функциональными возможностями. Мы используем только самые современные инструменты веб-разработки для достижения максимальных результатов. ЧИСТЫЙ КОД  мы не используем в своих проектах конструкторы сайтов, а делаем профессиональные индивидуальные продукты высокого качества АДАПТИВНОСТЬ наши сайты отлично отображаются на любых устройствах (ПК, планшет, смартфон) СОВРЕМЕННОСТЬ мы используем самые последние технологии Web-разработки для достижения максимальных результатов",
//            address = "г. Минск, Шугаево 171",
//            phones = "",
//            email = "mail@ilavista.by",
//            website = "ilavista.by"
//        )
//        databaseParent.push().setValue(corp1)
//////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИП Прокопчик И.Л.",
//            poster = "http://belorussia.su/com_logo/1577363654logo1_big.jpg",
//description = "Увеличивайте продажи и автоматизируйте бизнес процессы с Битрикс24. Комплексное внедрение и сопровождение при интеграции в Битрикс24 в Вашу компанию.",
//            address = "220007, г. Минск, Немига 5",
//            phones = "",
//            email = "info@proresult.by",
//            website = "https://proresult.by"
//        )
//        databaseParent.push().setValue(corp2)
////////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Исида-Информатика",
//            poster = "",
//description = "Исида-Информатика, ООО Общество с ограниченной ответственностью Год основания: 1994 Количество сотрудников: 20 УНП: 300247505",
//            address = "210015, г. Витебск, ул. Калинина, 4 (4 этаж)",
//            phones = "(0212)23-61-94",
//            email = "support@isida.by",
//            website = "www.isida.by"
//        )
//        databaseParent.push().setValue(corp3)
////////////
////////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Испаер Системс",
//            poster = "",
//description = "Испаер Системс, ООО Общество с ограниченной ответственностью Год основания: 2003 Количество сотрудников: 10 УНП: 190482272",
//            address = "220013, г. Минск, ул. Кульман, 2-431",
//            phones = "(017)292-61-61",
//            email = "ispirer@ispirer.com",
//            website = "www.ispirer.com"
//        )
//        databaseParent.push().setValue(corp4)
////////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИТ Парк",
//            poster = "",
//description = "ИТ Парк, ИЧПУП Иностранное предприятие Год основания: 2006 Количество сотрудников: 300 УНП: 190743344",
//            address = "220040, г. Минск, ул. М. Богдановича, 155-809",
//            phones = "(017)334-96-86",
//            email = "it.park@park.iba.by",
//            website = "www.iba.by"
//        )
//        databaseParent.push().setValue(corp5)
////////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Итворкс",
//            poster = "",
//description = "Итворкс, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 19 УНП: 290489791",
//            address = "224016, г. Брест, ул. Куйбышева, 13-7",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp6)
////////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИТ Навис",
//            poster = "http://belorussia.su/com_logo/1413286696logo1_big.jpg",
//description = "Имея богатый опыт в сфере интеграции программных комплексов мы можем с увереностью сказать, что Ваш бизнес может работать эффективнее. Мы знаем как задействовать Вашу ИТ инфраструктуру на 100% используя такие платформы как Amazon AWS, IBM Websphere MessageBroker, SAP, Mule, Apache Camel, Lucene. Наша главная цель - вывести вашу компанию на новый уровень путем анализа данных и интеграции программного обеспечения на единой корпоративной шине.",
//            address = "",
//            phones = "",
//            email = "info@itnavis.com",
//            website = "http://itnavis.by"
//        )
//        databaseParent.push().setValue(corp7)
////////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИТТАС",
//            poster = "http://belorussia.su/com_logo/1453199087logo1_big.jpg",
//description = "Ittas.by  - Разработка программных и программно-аппаратных средств криптографической защиты информации. Оказание услуг в области защиты информации. Наличие собственной аккредитованной испытательной лаборатории.\"\n",
//            address = "г. Минск, Короля 51",
//            phones = "",
//            email = "info@ittas.by",
//            website = "http://ittas.by"
//        )
//        databaseParent.push().setValue(corp8)
////////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Квазитекс",
//            poster = "",
//description = "Квазитекс, ЧСУП Частное предприятие Год основания: 2007 Количество сотрудников: 1УНП: 590710225",
//            address = "230020, г. Гродно, ул. Кабяка, 8/1-20",
//            phones = "(029)785-21-39",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
//////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "КИМ",
//            poster = "",
//description = "КИМ, ООО Общество с ограниченной ответственностью Год основания: 1994 Количество сотрудников: 10 УНП: 700217931",
//            address = "212030, г. Могилев, ул. Ленинская, 63-202, 203",
//            phones = "(0222)29-99-92",
//            email = "kim.mogilev@gmail.com",
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
//        lifecycleScope.launch {
//            fragmentViewModel.showShimmer
//                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect {
//                    with(binding) {
//                        when (it) {
//                            true -> {
//                                shimmerLayout.shimmer.visibility = View.VISIBLE
//                                statisticCard.statisticCardForView.visibility = View.GONE
//                            }
//
//                            false -> {
//                                statisticCard.statisticCardForView.visibility = View.VISIBLE
//                                shimmerLayout.shimmer.visibility = View.GONE
//                            }
//                        }
//                    }
//                }
//        }

        lifecycleScope.launch {
            fragmentViewModel.allCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collect {
                    if (it == 0) {
                        binding.shimmerLayout.shimmer.visibility = View.VISIBLE
                        binding.statisticCard.statisticCardForView.visibility = View.GONE
                    } else {
                        binding.statisticCard.statisticCardForView.visibility = View.VISIBLE
                        binding.shimmerLayout.shimmer.visibility = View.GONE
                        binding.statisticCard.countAllInBase.text = it.toString()
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

//    private fun initViews() {
//        updateStateBottomMenu.enableBottomMenu()
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}