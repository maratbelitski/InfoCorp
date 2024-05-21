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

//      удаление из пользовательской бд
//     databaseParent.child("-Ny0Mf6qyYQ4PMnx6NDk").removeValue()


//        сравнение из пользовательской бд  и добавление в основную
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
//            name = "Визутех Систем",
//            poster = "http://belorussia.su/com_logo/1417699742logo1_big.jpg",
//description = "Компания «Визутех Систем» занимается разработкой, поддержкой и сопровождением программного обеспечения для управления производственными процессами. Приоритетным направлением в деятельности нашего предприятия является разработка программных продуктов на основе технологии EtherCAT, которая занимает лидирующее положение среди промышленных высокоскоростных систем передачи данных в режиме реального времени. Также компания разрабатывает собственную библиотеку управления движением Motion Control и комплекс программных продуктов Automation для сбора, обработки, отображения и архивирования информации по объекту управления. За более чем 20 лет работы специалисты компании приняли участие в разработке проектов для таких компаний как BMW, Siemens, Volkswagen, Hypertherm и др., а также внедрили ПО на производственных линиях LG, Samsung, Foxconn, Hitachi, Intel, Mitsubishi и других ведущих производителей электроники. Обновление разрабатываемого ПО в соответствии с современными трендами",
//            address = "Республика Беларусь, 220004 г. Минск, ул. Клары Цеткин, 24-9",
//            phones = "+375 17 348 37 02",
//            email = "info@visutechsystem.by",
//            website = "www.visutechsystem.by"
//        )
//        databaseParent.push().setValue(corp1)
////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ВирусБлокАда",
//            poster = "",
//description = "ВирусБлокАда, ОДО Общество с дополнительной ответственностью Год основания: 1997 Количество сотрудников: 30 УНП: 101294617",
//            address = "220004, г. Минск, ул. Кальварийская, 17-611",
//            phones = "(017)226-85-55",
//            email = "support@anti-virus.by",
//            website = "www.anti-virus.by"
//        )
//        databaseParent.push().setValue(corp2)
//////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Витебский информационный центр",
//            poster = "",
//description = "Витебский информационно-производственный центр, ОАО Открытое акционерное общество Год основания: 1975 Количество сотрудников: 12 УНП: 300125159",
//            address = "210001, г. Витебск, ул. Димитрова, 4",
//            phones = "(0212)36-04-12, 36-33-62",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp3)
//////
//////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Виэм солюшенс",
//            poster = "",
//description = "Виэм солюшенс, ЧТСУП Частное предприятие Год основания: 2009 Количество сотрудников: 4",
//            address = "223051, Минская обл., Минский р-н, в/г Колодищи, 228-71",
//            phones = "(029)773-37-30",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp4)
//////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Влавас",
//            poster = "",
//description = "О компании Влавас, ПЧУП Влавас, ПЧУП Частное предприятие Год основания: 1995 Количество сотрудников: 30 УНП: 100121071",
//            address = "пр. газ. Звезда, д. 46, (оф. 45)",
//            phones = "8 017 277 28 50, 8 029 671 51 83",
//            email = "info@vlavas.by",
//            website = "www.vlavas.by"
//        )
//        databaseParent.push().setValue(corp5)
//////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Центр Белкоопсоюз",
//            poster = "",
//description = "Вычислительный центр ЧУП \"Белкоопсоюз\" Гродненский отдел Частное предприятие Год основания: 2001 Количество сотрудников: 3 УНП: 100105017",
//            address = "230025, г. Гродно, ул. Комсомольская, 4 (2 этаж)",
//            phones = "(0152)75-17-18",
//            email = "grodno@vc.bks.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp6)
//////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ВэбСити",
//            poster = "",
//description = "ВэбСити, ЧП Частное предприятие Год основания: 2009 УНП: 690577680",
//            address = "222810, Минская обл., г. Марьина Горка, ул. Элеваторная, 2/а-7",
//            phones = "(01713)4-18-04, (029)777-47-30",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
//////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Гелионтрайнсолюшинс",
//            poster = "",
//description = "Гелионтрайнсолюшинс, ООО Общество с ограниченной ответственностью Год основания: 2004",
//            address = "246061, г. Гомель, ул. Федюнинского, 17-26",
//            phones = "",
//            email = "mail@helion-prime.com",
//            website = "www.helion-prime.com"
//        )
//        databaseParent.push().setValue(corp8)
//////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Геоинформационные системы",
//            poster = "",
//description = "Геоинформационные системы, УП Государственное предприятие Год основания: 1997 Количество сотрудников: 60 УНП: 101151656",
//            address = "220012, г. Минск, ул. Сурганова, 6-511/а",
//            phones = "(017)284-28-60",
//            email = "zolotoy@itk2.bas-net.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Геокомпьютер",
//            poster = "",
//description = "Геокомпьютер, ООО Общество с ограниченной ответственностью Год основания: 1998 Количество сотрудников: 6 УНП: 400549368",
//            address = "246050, г. Гомель, ул. Тельмана, 44-203",
//            phones = "(0232)71-35-79",
//            email = "belvit@rambler.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Гечанский А. А.",
//            poster = "",
//description = "Гечанский А. А., ИП Предприниматель Год основания: 2005 Количество сотрудников: 1 УНП: 690335253",
//            address = "222140, Минская обл., Борисовский р-н, д. Демидовка, ул. Центральная, 1",
//            phones = "(029)501-46-06",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp11)
////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Главбух",
//            poster = "",
//description = "Главбух, ООО Общество с ограниченной ответственностью Год основания: 2003 Количество сотрудников: 20 УНП: 190496320",
//            address = "220002, г. Минск, ул. В. Хоружей, 33/а",
//            phones = "(017)200-99-66",
//            email = "info@gbsoft.by",
//            website = "www.gbsoft.by"
//        )
//        databaseParent.push().setValue(corp12)
////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Главбух-Гомель",
//            poster = "",
//description = "Главбух-Гомель, ООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 6 УНП: 190789934",
//            address = "246050, г. Гомель, ул. Подгорная, 10",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Гомельский вычислительный центр",
//            poster = "",
//description = "Гомельский вычислительный центр строителей, ОАО Открытое акционерное общество Год основания: 1970 Количество сотрудников: 13 УНП: 400010845",
//            address = "246044, г. Гомель, ул. Барыкина, 230",
//            phones = "(0232)42-79-61, 42-67-97, 42-69-12",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp14)
////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Грамотный PR",
//            poster = "http://belorussia.su/com_logo/1568588985logo1_big.jpg",
//description = "Компания «Грамотный PR» занимается разработкой с 2009 года, что позволило компании добиться различных достижений в области разработки web-сайтов, мобильных приложений и различного рода программных решений. Наша команда всегда рада помочь Вам, воплотить в жизнь нестандартное решение или же идею для Вашего бизнеса. Свяжитесь с нашими менеджерами и мы обязательно поможем Вам.",
//            address = "Минск, переулок Бехтерева 8",
//            phones = "+375 25 936-71-57, +375 25 953 91 80",
//            email = "info@g-pr.dev",
//            website = "https://g-pr.dev"
//        )
//        databaseParent.push().setValue(corp15)
//
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Гращенко С. В.",
//            poster = "",
//description = "Гращенко С. В., ИП Предприниматель Год основания: 2007 Количество сотрудников: 1 УНП: 790401680",
//            address = "212029, г. Могилев, пр-т Пушкинский, 75-128",
//            phones = "(0222)41-52-28",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp16)
//
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Гросс-Домен",
//            poster = "",
//            description = "Гросс-Домен, ООО Общество с ограниченной ответственностью Год основания: 2001 УНП: 190212692",
//            address = "220017, г. Минск, а/я 75",
//            phones = "(029)755-14-63",
//            email = "support@gd.by",
//            website = "www.gd.by"
//        )
//        databaseParent.push().setValue(corp17)
//
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Группа Айтек",
//            poster = "",
//            description = "Группа Айтек, ООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 10",
//            address = "220039, г. Минск, ул. Куйбышева, 22-2",
//            phones = "",
//            email = "info@itecgp.com",
//            website = "www.itecgroup.by"
//        )
//        databaseParent.push().setValue(corp18)
//
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Гуртсофт",
//            poster = "",
//            description = "Гуртсофт, ООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 14 УНП: 190871674",
//            address = "220140, г. Минск, ул. Лещинского, 10-2",
//            phones = "",
//            email = "info@gurtam.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp19)
////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Далис",
//            poster = "",
//            description = "Далис, УП Частное предприятие Год основания: 1997 Количество сотрудников: 2 УНП: 101311080",
//            address = "220012, г. Минск, пер. Калининградский, 8-9",
//            phones = "",
//            email = "soft-ad@tut.by",
//            website = ""
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