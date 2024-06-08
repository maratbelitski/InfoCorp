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
import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentGeneralBinding
import com.infocorp.presentation.logindisplay.LoginFragmentDirections
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

        // addCorps()
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
//            name = "Модис-М",
//            poster = "",
//description = "Модис-М, ЗАО Закрытое акционерное общество Год основания: 1994 Количество сотрудников: 25 УНП: 100165842",
//            address = "220088, г. Минск, ул. Смоленская, 15-509",
//            phones = "(017)290-44-18",
//            email = "info@modis-m.by",
//            website = "www.modis-m.by"
//        )
//        databaseParent.push().setValue(corp1)
//////////////////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Морэйн",
//            poster = "http://belorussia.su/com_logo/1453199291logo1_big.jpg",
//description = "Morane.by - Разработка, интеграция и сопровождение бизнес-решений. Собственная разработка системы электронного документооборота DocNode, уникальный продукт DocNode SaaS. Компания включена в список разработчиков, обеспечивших интеграцию ВСЭД с СМДО.",
//            address = "г. Минск, ул. Короля, 51",
//            phones = "",
//            email = "info@morane.by",
//            website = "http://morane.by"
//        )
//        databaseParent.push().setValue(corp2)
//////////////////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "МэтаСистем",
//            poster = "http://belorussia.su/com_logo/1399467648logo1_big.jpg",
//description = "Мы – web-студия «МэтаСистем»,  создаём сайты с 2012 года. Мы любим нашу работу и наши проекты, которые делаем на отлично, чтобы они максимально соответствовали  поставленным задачам и приносили пользу.",
//            address = "220073, г. Минск, ул. Гусовского, 2а, (2 этаж, офис 2-6)",
//            phones = "+375(17) 204 34 11, +375(29) 602 11 48",
//            email = "info@metasystem.by",
//            website = "http://metasystem.by"
//        )
//        databaseParent.push().setValue(corp3)
//////////////////
//////////////////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Центр мед. технологий",
//            poster = "",
//description = "Научно-практический республиканский центр медицинских технологий, информатизации управления и экономики здравоохранения Государственное предприятие Год основания: 1992 Количество сотрудников: 125",
//            address = "220600, г. Минск, ул. П. Бровки, 7/а",
//            phones = "(017)292-30-80, 292-30-94, 331-32-04,  331-31-30",
//            email = "belcmt2@mail.belpak.by",
//            website = "http://minzdrav.by"
//        )
//        databaseParent.push().setValue(corp4)
//////////////////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "НаучСофт",
//            poster = "",
//description = "НаучСофт, СП ЗАО Закрытое акционерное общество Год основания: 1996 Количество сотрудников: 100 УНП: 101132899",
//            address = "220040, г. Минск, ул. Беды, 2 (4 этаж)",
//            phones = "(017)293-37-35, 293-37-36",
//            email = "info@scnsoft.com",
//            website = "www.scnsoft.com"
//        )
//        databaseParent.push().setValue(corp5)
//////////////////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Нетлоджик",
//            poster = "",
//description = "Нетлоджик, ИП Иностранное предприятие Год основания: 2004 Количество сотрудников: 15",
//            address = "212002, г. Могилев, ул. Резервная, 9-31",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp6)
//////////////////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "НИИЭВМсервис",
//            poster = "",
//description = "НИИЭВМсервис, ООО Общество с ограниченной ответственностью Год основания: 1991 Количество сотрудников: 80 УНП: 100160718",
//            address = "220040, г. Минск, ул. М. Богдановича, 155",
//            phones = "(017)334-57-33, 331-03-96",
//            email = "niiserv@iba.by",
//            website = "www.niiserv.iba.by"
//        )
//        databaseParent.push().setValue(corp7)
//////////////////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Нилитис",
//            poster = "",
//description = "Нилитис, ОДО Общество с дополнительной ответственностью Год основания: 1998 Количество сотрудников: 7 УНП: 101527160",
//            address = "220040, г. Минск, ул. Беды, 2 (3 этаж)",
//            phones = "(017)266-21-31",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp8)
//////////////////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Новаком Проект",
//            poster = "",
//description = "Новаком Проект, ООО Общество с ограниченной ответственностью Год основания: 2002 Количество сотрудников: 12 УНП: 190395635",
//            address = "220006, г. Минск, ул. Семенова, 1/а-10",
//            phones = "(017)328-32-95",
//            email = "nproject@nvcm.net",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
////////////////////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Новософт",
//            poster = "",
//description = "Новософт, ООО Общество с ограниченной ответственностью Год основания: 2004 Количество сотрудников: 25 УНП: 390316790",
//            address = "211440, Витебская обл., г. Новополоцк, ул. Калинина, 1",
//            phones = "(0214)53-62-46",
//            email = "mail@novosoft.by",
//            website = "http://novosoft.by"
//        )
//        databaseParent.push().setValue(corp10)
//////////////////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Новые аналитические системы",
//            poster = "",
//description = "Новые аналитические системы, ООО Общество с ограниченной ответственностью Год основания: 1994 Количество сотрудников: 6 УНП: 100300590",
//            address = "220089, г. Минск, ул. Грушевская, 124-109",
//            phones = "",
//            email = "unichrom@unichrom.com",
//            website = "www.unichrom.com"
//        )
//        databaseParent.push().setValue(corp11)
//////////////////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Новый поворот",
//            poster = "",
//description = "Новый поворот, ЧПУП Частное предприятие Год основания: 2007 Количество сотрудников: 5 УНП: 190871419",
//            address = "г. Минск, ул. Чеботарева, 2/4, а/я 141",
//            phones = "",
//            email = "",
//            website = "www.cd.ppd.by"
//        )
//        databaseParent.push().setValue(corp12)
//////////////////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "НТЛаб-Системы",
//            poster = "",
//description = "НТЛаб-Системы, ПЧУП Частное предприятие Год основания: 2007 Количество сотрудников: 70 УНП: 190804788",
//            address = "220013, г. Минск, ул. Сурганова, 41-420",
//            phones = "(017)290-09-99",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
//////////////////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "НТЦ Контакт",
//            poster = "",
//description = "НТЦ Контакт, ЗАО Закрытое акционерное общество Год основания: 1990 Количество сотрудников: 17 УНП: 100037461",
//            address = "220034, г. Минск, ул. Первомайская, 17-1",
//            phones = "(017)233-95-68",
//            email = "kontakt@mail.bn.by",
//            website = "www.vmssoft.com"
//        )
//        databaseParent.push().setValue(corp14)
//////////////////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Нью Ап",
//            poster = "http://belorussia.su/com_logo/1493732199logo1_big.jpg",
//description = "Команда New Up оказывает услуги продвижения сайтов в поисковых системах и поддержки веб-сайтов организаций и компаний, так же разрабатываем уникальные сайты. Не мало важной задачей компании является IT-маркетинг, который с каждым годом меняется с учетом потребностей и запросов клиентов.",
//            address = "ул. Жилуновича 15, (офис 307)",
//            phones = "+375 17 396-41-50, +375 44 771-53-48, +375 29 336-90-73",
//            email = "info@webnewup.by",
//            website = "http://webnewup.by"
//        )
//        databaseParent.push().setValue(corp15)
////////////
//        val corp16 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ПрогзИТ",
//            poster = "",
//description = "ProgzIT предлагает услуги по разработке программного обеспечения по индивидуальному заказу. Компания осуществляет весь цикл разработки:  создание, внедрение, сопровождение и развитие IT-систем любой сложности, обучение сотрудников заказчика.",
//            address = "г.Минск,пр-т Дзержинского, 104А, офис 802",
//            phones = "+375 (17) 336 06 80",
//            email = "info@progz.ru",
//            website = "http://progz.by"
//        )
//        databaseParent.push().setValue(corp16)
////////////
//        val corp17 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Оверлей",
//            poster = "",
//description = "Оверлей, ЗАО Закрытое акционерное общество Год основания: 1996 Количество сотрудников: 8 УНП: 101085493",
//            address = "220030, г. Минск, ул. Свердлова, 23-107",
//            phones = "(017)226-03-32",
//            email = "overlay.csc@open.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp17)
////////////
//        val corp18 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "ИНТЕРМЕХ",
//            poster = "http://belorussia.su/com_logo/1339406881logo1_big.jpg",
//description = "Разработка и внедрение САПР машиностроения, приборостроения и строительства. Разработанный нами гибкий интегрированный комплекс программ обеспечивает работу конструкторских, технологических и других служб предприятия в едином информационном пространстве. Используемые платформы: AutoCAD, Inventor, NX Siemens PLM Software, SolidWorks, Pro Engineer, Creo, Solid Edge, Bricscad.",
//            address = "Республика Беларусь, 220004, Минск, ул. Короля, 51",
//            phones = "(+375 17) 306-21-30, 306-21-32, 306-21-35, 306-21-36, 306-21-37",
//            email = "",
//            website = "http://intermech.by"
//        )
//       databaseParent.push().setValue(corp18)
////////////
//        val corp19 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Озон Консалтинг",
//            poster = "",
//description = "Озон Консалтинг, СООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 16 УНП: 190819554",
//            address = "220074, г. Минск, ул. Харьковская, 58, оф.С",
//            phones = "(017)251-99-65",
//            email = "info@ozone-c.com",
//            website = "http://ozone-c.com"
//        )
//        databaseParent.push().setValue(corp19)
//////////////
//        val corp20 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Омертекс",
//            poster = "http://belorussia.su/com_logo/1402044582logo1_big.jpg",
//description = "Омертекс – аутсорсинговая IT-компания, основанная в Беларуси, которая предоставляет весь спектр надежных технических решений в сфере разработки программных продуктов. Мы помогаем компаниям автоматизировать свои бизнес-процессы и увеличить свой капитал, используя все преимущества передовых технологий. Наши высококвалифицированные специалисты в области IT-технологий готовы предложить вам самые современные решения. ИСТОРИЯ КОМПАНИИ. Основанная в 2009 году, компания Омертекс развивается и в настоящее время предоставляет услуги по разработке программного обеспечения на рынках России, Северной Америки и Европы. Наша команда включает в себя более 25 квалифицированных специалистов в области IT-технологий. Основатели и ключевые фигуры нашей компании, в прошлом ведущие сотрудники крупнейших аутсорсинговых компаний Восточной Европы, обладают 12-летним опытом разработки программного обеспечения. В то же время, остальные наши специалисты имеют значительный опыт оффшорного программирования, проработав в этой сфере не менее трех лет.",
//            address = "220026, г. Минск, ул. Жилуновича, 15, (офис 401)",
//            phones = "8-017-346-92-04",
//            email = "natalia@omertex.com",
//            website = "http://omertex.com"
//        )
//        databaseParent.push().setValue(corp20)
   }
}