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


class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw Exception()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
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
        updateStateBottomMenu.enableBottomMenu()



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



//        val corp1 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Аксофтбел",
//            poster = "",
//            description = "Аксофтбел, ООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 6 УНП: 190675852",
//            address = "220040, г. Минск, ул. М. Богдановича, 155-1217а",
//            phones = "(017)290-77-93",
//            email = "info@axoft.by",
//            website = "www.axoft.by"
//        )
//        databaseParent.push().setValue(corp1)
//
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Активные технологии",
//            poster = "",
//            description = "Активные технологии, ЧУП Частное предприятие Год основания: 2004",
//            address = "220121, г. Минск, ул. Притыцкого, 39, помещение 1н",
//            phones = "(017)209-43-88",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp2)
//
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Алгоритм",
//            poster = "",
//description = "Алгоритм, ОДООбщество с дополнительной ответственностью Год основания: 1990 Количество сотрудников: 30 УНП: 500019969",
//            address = "230023, г. Гродно, ул. Ленина, 13 (3 этаж)",
//            phones = "(0152)77-00-40, 72-14-50, (029)781-47-98,  (029)332-26-00",
//            email = "algo@mail.grodno.by",
//            website = "www.algo.grodno.by"
//        )
//        databaseParent.push().setValue(corp3)
//
//
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Алгоритмы и системы",
//            poster = "",
//description = "Алгоритмы и системы, ЗАО Закрытое акционерное общество Год основания: 1995 Количество сотрудников: 2 УНП: 100967226",
//            address = "220113, г. Минск, ул. Я. Коласа, 73-702",
//            phones = "(017)262-81-36",
//            email = "office@alsy.by",
//            website = "www.alsy.by"
//        )
//        databaseParent.push().setValue(corp4)
//
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Альтграфик",
//            poster = "",
//description = "Альтграфик, ОДО Общество с дополнительной ответственностью Год основания: 1996 Количество сотрудников: 10 УНП: 190722293",
//            address = "220040, г. Минск, ул. М. Богдановича, 155/8-114",
//            phones = "(029)750-50-57, (029)384-84-35",
//            email = "info@altgraphic.com",
//            website = "www.altgraphic.com"
//        )
//        databaseParent.push().setValue(corp5)
//
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Альфета системс",
//            poster = "",
//            description = "Альфета системс, ООООбщество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 10 УНП: 190804696",
//            address = "220073, г. Минск, пер. 4-й Загородный, 58/б-24",
//            phones = "(017)228-29-97, (029)635-80-34",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp6)
//
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Аналитик-Сервис",
//            poster = "",
//            description = "Аналитик-Сервис, УП Частное предприятие Год основания: 1991 Количество сотрудников: 11 УНП: 100041294",
//            address = "220053, г. Минск, ул. Зацень, 27/а, а/я 163",
//            phones = "(017)310-17-64, 310-17-65, (029)652-54-23, (029)767-86-49,  (029)368-30-50",
//            email = "analitiks@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
//
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Ананенко В. А.",
//            poster = "",
//description = "Ананенко В. А., ИП Предприниматель Год основания: 2007 Количество сотрудников: 1 УНП: 700194255",
//            address = "212000, г. Могилев, ул. Бонч-Бруевича, 4/4",
//            phones = "(0222)28-75-17",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp8)
//
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Андерсан",
//            poster = "",
//description = "Андерсан, ООО Общество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 15",
//            address = "220030, г. Минск, ул. Первомайская, 14-316",
//            phones = "(029)755-21-23",
//            email = "info@andersensoft.by",
//            website = "http://andersensoft.com"
//        )
//        databaseParent.push().setValue(corp9)
//
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Анкуда Д. О.",
//            poster = "",
//description = "Анкуда Д. О., ИП Предприниматель Год основания: 2009 Количество сотрудников: 1 УНП: 690573792",
//            address = "220104, г. Минск, ул. Глебки, 58-165",
//            phones = "(017)255-65-71",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
//
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Антерра",
//            poster = "",
//description = "Антерра, ООО Общество с дополнительной ответственностью Год основания: 2010 Количество сотрудников: 2 УНП: 690798998",
//            address = "222160, Минская обл., г. Жодино, ул. Кузнечная, 16",
//            phones = "(044)797-14-24",
//            email = "manager@inrb.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp11)
//
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Аргеон",
//            poster = "",
//description = "Аргеон, НП ООО Общество с ограниченной ответственностью Год основания: 1994 Количество сотрудников: 12 УНП: 100694744",
//            address = "220012, г. Минск, ул. Сурганова, 2/в-24",
//            phones = "",
//            email = "argeon@tut.by",
//            website = "www.argeon.org"
//        )
//        databaseParent.push().setValue(corp12)
//
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "АРЛЕС",
//            poster = "http://belorussia.su/com_logo/1374255452logo1_big.jpg",
//description = "АРЛЕС – это белорусское Интернет-агентство сделавшее свой первый проект еще в 2005 году, которое само того не ожидая, в один прекрасный день, стало международным. Мы начали создавать проекты для Российского рынка для Европы и для Америки. Мы открытый и надежный партнер в океане бизнеса. За правило мы взяли следующие слова: НАШ собственный успех прямопропорционален (напрямую зависит от) Успеху наших клиентов",
//            address = "Республика Беларусь, 220034, г. Минск, ул. Зм. Бядули, д. 15, комн. 107",
//            phones = "+375 (29) 2993993, +375 (44) 7993993",
//            email = "",
//            website = "http://www.arles.by"
//        )
//        databaseParent.push().setValue(corp13)
//
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Атлас Софт",
//            poster = "",
//description = "Атлас Софт, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 13 УНП: 191052841",
//            address = "220004, г. Минск, ул. Мельникайте, 4-406",
//            phones = "",
//            email = "j.masay@atlassoft.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp14)
//
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Б Софт Лаборатория",
//            poster = "",
//description = "Б Софт Лаборатория, ЗАО Закрытое акционерное общество Год основания: 2003 УНП: 190491117",
//            address = "220007, г. Минск, ул. Московская, 18-327",
//            phones = "",
//            email = "amir@belsoft.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
  }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}