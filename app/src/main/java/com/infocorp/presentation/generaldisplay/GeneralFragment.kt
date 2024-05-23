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
//            name = "Датаскан",
//            poster = "",
//description = "Датаскан, ЧП Частное предприятие Год основания: 1996 Количество сотрудников: 8 УНП: 101180244",
//            address = "220131, г. Минск, ул. Хмаринская, 13",
//            phones = "+(017)266-44-55",
//            email = "office@datascan.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp1)
//////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Дельта-1",
//            poster = "",
//description = "Дельта-1, ООО ПК Общество с ограниченной ответственностью Год основания: 1995 Количество сотрудников: 10 УНП: 400323181",
//            address = "246031, г. Гомель, ул. Рощинская, 2 (1 этаж)",
//            phones = "(0232)63-10-88",
//            email = "delta1@mail.gomel.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp2)
////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ДжейТорг",
//            poster = "",
//description = "ДжейТорг, ООООбщество с ограниченной ответственностью Год основания: 1997 Количество сотрудников: 5 УНП: 101385803",
//            address = "220012, г. Минск, ул. Сурганова, 7-202",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp3)
////////
////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Джи 2 экс",
//            poster = "",
//description = "Джи 2 экс Девелопмент Сервисис, ИП Иностранное предприятие Год основания: 2003 Количество сотрудников: 60 УНП: 800019013",
//            address = "220113, г. Минск, ул. Восточная, 133-801, 804",
//            phones = "",
//            email = "",
//            website = "www.j2x.com"
//        )
//        databaseParent.push().setValue(corp4)
////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Диалог-ПРО",
//            poster = "",
//description = "Диалог-ПРО, ЗАО Закрытое акционерное общество Год основания: 1991 Количество сотрудников: 12 УНП: 300237332",
//            address = "210026, г. Витебск, ул. Суворова, 36",
//            phones = "8 017 277 28 50, 8 029 671 51 83",
//            email = "odialog@vitebsk.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp5)
////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Дисплей Нетворкc",
//            poster = "",
//description = "Дисплей Нетворкc, ЧУП Частное предприятие Год основания: 2004 Количество сотрудников: 10 УНП: 190579638",
//            address = "220037, г. Минск, пер. Козлова, 7-15",
//            phones = "(017)227-70-08",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp6)
////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Ждан",
//            poster = "",
//description = "Ждан, ЧП Частное предприятие Год основания: 2007 Количество сотрудников: 5 УНП: 190882019",
//            address = "220102, г. Минск, ул. Алтайская, 64/1-02",
//            phones = "(017)297-45-72, (029)401-21-69",
//            email = "po@po.by",
//            website = "www.po.by"
//        )
//        databaseParent.push().setValue(corp7)
////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Живые программы",
//            poster = "",
//description = "Живые программы, ОДО Общество с дополнительной ответственностью Год основания: 2008 Количество сотрудников: 6 УНП: 290493818",
//            address = "225051, Брестская обл., г. Каменец, ул. Пограничная, 6",
//            phones = "(01631)6-84-08, (029)202-00-62",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp8)
////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Золотые программы",
//            poster = "",
//description = "Золотые программы, ОДО Общество с дополнительной ответственностью Год основания: 1994 Количество сотрудников: 30 УНП: 101106383",
//            address = "220073, г. Минск, ул. Скрыганова 6, оф. 2-204",
//            phones = "(017)256-17-59, 256-27-82, 256-27-83, (029)775-85-70,  (044)718-37-87",
//            email = "support@gsbelarus.com",
//            website = "www.gsbelarus.com"
//        )
//        databaseParent.push().setValue(corp9)
//////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИВЦ-Молодечно",
//            poster = "",
//description = "ИВЦ-Молодечно, ЧУП Частное предприятие Год основания: 1976 Количество сотрудников: 38 УНП: 600078148",
//            address = "222310, Минская обл., г. Молодечно, ул. Металлистов, 1 (5 этаж)",
//            phones = "(0176)75-38-72, 76-34-14",
//            email = "ivc_molodechno@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
//////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИГ Копейка",
//            poster = "",
//description = "ИГ Копейка, ООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 5 УНП: 190877856",
//            address = "220072, г. Минск, ул. Одоевского, 52-312",
//            phones = "(017)256-29-14, (029)656-21-21",
//            email = "sales@kopeyka.by",
//            website = "http://kopeyka.by"
//        )
//        databaseParent.push().setValue(corp11)
//////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Игроматик",
//            poster = "",
//description = "Игроматик, СООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 30 УНП: 190835366",
//            address = "220086, г. Минск, ул. Славинского, 12-512",
//            phones = "(017)263-25-50",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp12)
//////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Импет",
//            poster = "",
//description = "Импет, СЗАО Закрытое акционерное общество Год основания: 1991 Количество сотрудников: 23 УНП: 100003261",
//            address = "220108, г. Минск, ул. Казинца, 90/3",
//            phones = "(017)278-43-10, 212-09-50",
//            email = "mail@impet.com",
//            website = "www.impet.com"
//        )
//        databaseParent.push().setValue(corp13)
//////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инвеншион машин",
//            poster = "",
//description = "Инвеншион машин, ИП Иностранное предприятие Год основания: 2004 Количество сотрудников: 75 УНП: 190591204",
//            address = "220123, г. Минск, ул. Старовиленская, 131-407",
//            phones = "(017)293-14-99",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp14)
//////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Индсофтсистем",
//            poster = "",
//description = "Индсофтсистем, НПУП Частное предприятие Год основания: 1995 Количество сотрудников: 10",
//            address = "220141, г. Минск, ул. Ф. Скорины, 51-609",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инис-софт",
//            poster = "",
//description = "Инис-софт, НП ООО Общество с ограниченной ответственностью Год основания: 1994 Количество сотрудников: 15 УНП: 100434847",
//            address = "220004, г. Минск, ул. Короля, 51, помещение 10, офис 3 (5 этаж)",
//            phones = "(017)200-29-16",
//            email = "market@inissoft.by",
//            website = "www.inissoft.by"
//        )
//        databaseParent.push().setValue(corp16)
////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инсайтика",
//            poster = "http://belorussia.su/com_logo/1645516074logo1_big.jpg",
//description = "Маркетинговое агентство Инсайтика предлагает широкий спектр услуг по продвижению бизнеса в интернете. От создания продающего и живого сайта, рекламы, дизайна до комплексного продвижения компаний. Мы оказываем услуги: Разработка веб-сайтов Продвижение (SEO, SMM, Контекстная реклама)",
//            address = "230025, г. Гродно, ул. Ленина 5, офис 74.",
//            phones = "+375 (33) 637-30-79",
//            email = "info@insaitika.com",
//            website = "https://insaitika.com"
//        )
//        databaseParent.push().setValue(corp17)
////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Институт программных систем",
//            poster = "",
//description = "Институт прикладных программных систем, НИ РУП Государственное предприятиеГод основания: 1965 Количество сотрудников: 120УНП: 100059271",
//            address = "220013, г. Минск, ул. Беломорская, 18-209",
//            phones = "(017)290-07-63, 290-07-76, (029)649-44-21",
//            email = "info@ipps.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp18)
////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Институт системного анализа",
//            poster = "",
//description = "Институт системного анализа и информационного обеспечения научно-технической сферы Белорусский (БелИСА) Государственное предприятие Год основания: 1997 Количество сотрудников: 83 УНП: 101179888",
//            address = "220004, г. Минск, пр-т Победителей, 7-1213 (12-й этаж)",
//            phones = "(017)203-14-87",
//            email = "isa@belisa.org.by",
//            website = "www.belisa.org.by"
//        )
//        databaseParent.push().setValue(corp19)
//////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Инструментальные технологии",
//            poster = "",
//description = "Инструментальные технологии, ЗАО Закрытое акционерное общество Год основания: 1997 Количество сотрудников: 21 УНП: 101269853",
//            address = "220141, г. Минск, ул. Купревича, 1/3-404",
//            phones = "(017)265-91-51",
//            email = "intech@belsonet.net",
//            website = "www.intech.belsonet.net"
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