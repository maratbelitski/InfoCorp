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

//    private fun addCorps() {
////       основная бд
//        val databaseParent = Firebase.database.getReference(Constants.GENERAL_DB.value)
////       пользовательская бд
//        val databaseChild = Firebase.database.getReference(Constants.USER_DB.value)
//
////      удаление из пользовательской USER_CORPORATION бд
////     databaseChild.child("-Ny0Mf6qyYQ4PMnx6NDk").removeValue()
//
//
////        сравнение из пользовательской бд и добавление в основную
//
////        databaseChild.addValueEventListener(object : ValueEventListener{
////            override fun onDataChange(snapshot: DataSnapshot) {
////                val response = snapshot.children
////                response.forEach { child ->
////                    val childValue = child.getValue(CorporationDto::class.java)
////
////                    if (child.key == "-NxdQoErgl8ch0S9XdSn" ){
////                       databaseParent.push().setValue(childValue)
////                   }
////                }
////            }
////            override fun onCancelled(error: DatabaseError) { TODO() }
////        })
//
////
////
////        val corp1 = CorporationDto(
////            idFirebase = databaseParent.key.toString(),
////            name = "КОМПИТ ЭКСПЕРТ",
////            poster = "",
////            address = "220017 г. Минск,ул. Притыцкого, 156, 9 этаж, помещение 14",
////description = "ООО «КОМПИТ ЭКСПЕРТ» работает на рынке информационных технологий Республики Беларусь более 27 лет и имеет большой опыт реализации комплексных проектов по автоматизации бизнес-процессов, разработке, внедрению, технической поддержке и сопровождению информационных систем различной степени сложности, является центром компетенций по технологическому стеку работы с данными и их использованию. Мы создаем надежный фундамент для развития и процветания наших заказчиков и партнеров, внедряя новейшие высокотехнологичные и эффективные решения, которые отвечают целям и задачам современного бизнеса. НАПРАВЛЕНИЯ БИЗНЕСА: Работа с большими данными и системами аналитики. Цифровая трансформация бизнес-процессов. Консалтинг и аудит. Техническая поддержка и сопровождение",
////            phones = "+37517 370 86 60",
////            email = "info@compit.by",
////            website = "http://compit.by"
////        )
////        databaseParent.push().setValue(corp1)
//
//        val corp1 = CorporationDto(
//            id = database.key.toString(),
//            name = "Альфа-Гомель",
//            poster = "http://belorussia.su/com_logo/1422426178logo1_big.jpg",
//            description = "Компания «Альфа» предлагает Вашему вниманию весь спектр услуг в сфере программного обеспечения!Для  автоматизации документооборота предприятий нашими специалистами разработан комплекс готовых программ Альфа-Офис.Кроме того в сфере информационных технологий мы можем предложить комплексную защиту от воздействия вредоносных программ (ESET, лаборатория Касперского, Dr.Web, VBA32); разработка и/или доработка программного обеспечения; сопровождение программных продуктов; разработка, продвижение и сопровождение Web-сайтов; консультирование в области организации автоматизированного учета; поставка и настройка программных продуктов сторонних разработчиков (  ПО Microsoft, графическо-го ПО и многих других).Также мы предоставляем услуги по установке, настройке и сопровождению телекоммуникационных систем, мини АТС (запись разговоров, оптимизация расходов между филиалами одного предприятия, сохранение номера при переезде в новое помещение,   снижение затрат на связь, детализация звонков, интеграция с CRM, голосовое меню, группы вызова, безопасность, видеозвонки, конференцсвязь и другое).",
//            address = "246000, Республика Беларусь, г.Гомель, ул. Федюнинского 17",
//            phones = "+375 (232) 68-27-39, 68-26-44,+375(29)168-27-39, +375(33)682-74-97, +375(25)603-27-44",
//            email = "sales@alfagomel.by",
//            website = "http://alfagomel.by"
//        )
//        val corp2 = CorporationDto(
//            id = database.key.toString(),
//            name = "1С-Минск",
//            description = "Компания СООО «1С-Минск» занимается автоматизацией управления и учета на базе программных продуктов «1С». СООО «1С-Минск» — совместное предприятие Фирмы «1С» (г. Москва)",
//            address = "г. Минск, ул. Шафарнянская, д.11, офис 104",
//            phones = "+375-17-360-85-50",
//            email = "office@1c-minsk.by",
//            website = "www.1c-minsk.by"
//        )
//        val corp3 = CorporationDto(
//            id = database.key.toString(),
//            name = "4Д",
//            description = "4Д, ОДООбщество с дополнительной ответственностью. Год основания: 2008. Количество сотрудников: 6. УНП: 590830939",
//            address = "230023, г. Гродно, ул. 17 Сентября, 49-309, 310",
//            phones = "(029)189-58-88"
//        )
//    }
}