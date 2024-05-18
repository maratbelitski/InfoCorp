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

//
//
//        val corp1 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Балашов Ю.В",
//            poster = "",
//description = "Балашов Ю. В., ИП Предприниматель Год основания: 2005 Количество сотрудников: 1",
//            address = "231400, Гродненская обл., г. Новогрудок, ул. Булгака, 21",
//            phones = "(029)634-11-60",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp1)
////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белабат",
//            poster = "",
//            description = "Белабат, СООО Общество с ограниченной ответственностью Год основания: 2004 Количество сотрудников: 21 УНП: 190549007",
//            address = "220012, г. Минск, пер. Калининградский, 8-2",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp2)
//
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белаис",
//            poster = "",
//description = "Белаис, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 8 УНП: 190851152",
//            address = "220090, г. Минск, Логойский тракт, 22-2303",
//            phones = "",
//            email = "vi@belais.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp3)
//
//
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БелАсусИнК",
//            poster = "",
//description = "БелАсусИнК, СООО Общество с ограниченной ответственностью Год основания: 2005 Количество сотрудников: 4 УНП: 390353758",
//            address = "211440, Витебская обл., г. Новополоцк, ул. Юбилейная, 2/а-323",
//            phones = "(0214)59-43-91, (029)397-39-73",
//            email = "office@belasusinc.com",
//            website = "www.belasusinc.com"
//        )
//        databaseParent.push().setValue(corp4)
//
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БелДТС",
//            poster = "",
//description = "БелДТС, СООО Общество с ограниченной ответственностью Год основания: 2000 Количество сотрудников: 15 УНП: 800002099",
//            address = "220073, г. Минск, ул. Ольшевского, 22-15",
//            phones = "",
//            email = "contact@beldts.de",
//            website = ""
//        )
//        databaseParent.push().setValue(corp5)
//
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белинвестлес",
//            poster = "",
//            description = "Белинвестлес, ОДО Общество с дополнительной ответственностью Год основания: 2000 Количество сотрудников: 10 УНП: 100133857",
//            address = "220015, г. Минск, ул. Гурского, 32-410",
//            phones = "",
//            email = "belinvestles@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp6)
//
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БелИнфоСфера",
//            poster = "",
//            description = "БелИнфоСфера, ЧУП Частное предприятие Год основания: 2007 Количество сотрудников: 5",
//            address = "213809, Могилевская обл., г. Бобруйск, ул. Чонгарская, 98-33",
//            phones = "(0225)44-55-40",
//            email = "belinfosfera@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
//
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белитсофт",
//            poster = "",
//description = "Белитсофт, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 60",
//            address = "220037, г. Минск, пер. Козлова, 7, помещение 18",
//            phones = "(029)875-55-85",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp8)
//
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белорусский дорожный инженерно-технический центр",
//            poster = "",
//description = "Белорусский дорожный инженерно-технический центр, РУП Государственное предприятие Год основания: 1987 Количество сотрудников: 600 УНП: 190638734",
//            address = "220036, г. Минск, пер. Домашевский, 11",
//            phones = "(017)208-67-78, 208-80-00, 259-76-02,  259-76-25, 259-78-25,  259-76-18",
//            email = "office@bdcmtk.by",
//            website = "www.beldor.centr.by"
//        )
//        databaseParent.push().setValue(corp9)
////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белприминвест",
//            poster = "",
//description = "Белприминвест, ООО Общество с ограниченной ответственностью Год основания: 2008 Количество сотрудников: 12 УНП: 191040331",
//            address = "220113, Минск, ул. Мележа 1-718",
//            phones = "",
//            email = "shop@bigshop.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
////
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
////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белсек",
//            poster = "",
//description = "Белсек, СООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 10 УНП: 190764436",
//            address = "220082, г. Минск, ул. Притыцкого, 34-4 (3 подъезд, 2 этаж)",
//            phones = "(017)216-91-18",
//            email = "info@zadvinie.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp12)
////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белсофт-Борлас Групп",
//            poster = "http://belorussia.su/com_logo/1374255452logo1_big.jpg",
//description = "Белсофт-Борлас Групп, СЗАО Закрытое акционерное обществоГод основания: 2006 Количество сотрудников: 55 УНП: 190726789",
//            address = "220004, г. Минск, пр-т Победителей, 23/1-322",
//            phones = "(017)204-85-36",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белстипф",
//            poster = "http://belorussia.su/com_logo/1416316286logo1_big.jpg",
//description = "Разработка программных решений для автоматизации розничной торговли сети магазинов в Республике Беларусь. Разработка программного обеспечения для систем безопасности: контроль доступа и видео.",
//            address = "220113 г.Минск, ул.Мележа 5/2, ком.1503",
//            phones = "+375-17-2684561",
//            email = "office@stiepf.net",
//            website = "http://www.stiepf.net"
//        )
//        databaseParent.push().setValue(corp14)
////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БелТехноСофт",
//            poster = "",
//description = "БелТехноСофт, ЧУП Частное предприятие Год основания: 2000 Количество сотрудников: 30 УНП: 590647166",
//            address = "230023, г. Гродно, ул. Буденного, 48/а-66, 63, 65, 67, 68",
//            phones = "(0152)72-30-81, (029)782-71-27",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp15)
  }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}