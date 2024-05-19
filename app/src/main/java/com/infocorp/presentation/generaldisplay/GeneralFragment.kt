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
//            name = "БелТехноСофт",
//            poster = "",
//description = "БелТехноСофт, ЧУП Частное предприятие Год основания: 2000 Количество сотрудников: 30 УНП: 590647166",
//            address = "230023, г. Гродно, ул. Буденного, 48/а-66, 63, 65, 67, 68",
//            phones = "(0152)72-30-81, (029)782-71-27",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp1)
//////
//        val corp2 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белтехсервис-Про",
//            poster = "",
//description = "Белтехсервис-Про, ОДО Общество с дополнительной ответственностью Год основания: 2000 Количество сотрудников: 5 УНП: 490176373",
//            address = "246028, г. Гомель, ул. Головацкого, 19-206",
//            phones = "",
//            email = "beltehpro@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp2)
////
//        val corp3 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белтранском",
//            poster = "",
//description = "Белтранском, ОДО Общество с дополнительной ответственностью Год основания: 1991 Количество сотрудников: 15 УНП: 100136088",
//            address = "220114, г. Минск, ул. Филимонова, 63",
//            phones = "(017)237-02-38",
//            email = "beltranscom@tut.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp3)
////
////
//        val corp4 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Белфортекс",
//            poster = "http://belorussia.su/com_logo/1339661337logo1_big.jpg",
//description = "Сферой наших коммерческих интересов является инновационная деятельность в области интеллектуальной продукции и высоких технологий. Одно из направлений нашей работы — научные исследования и экспериментальные работы по созданию специального математического и программного обеспечения для систем управления военного назначения. Программные продукты предприятия «Белфортекс» прошли опытную эксплуатацию в ходе командно-штабных учений и используются в повседневной деятельности войск Республики Беларусь. В сотрудничестве с нашими партнерами мы разрабатываем и производим специальную технику, предназначенную для противодействия терроризму. Наша спецтехника принята на боевое дежурство во внутренних войсках министерства внутренних дел Республики Беларусь.",
//            address = "220017, г. Минск, ул. Притыцкого, 146, г. Минск, ул. Заславская, 10, УНН – 100167207",
//            phones = "(017) 256-86-91",
//            email = "support@belfortex.com",
//            website = "http://belfortex.com"
//        )
//        databaseParent.push().setValue(corp4)
////
//        val corp5 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Бизнес-системы",
//            poster = "",
//description = "Бизнес-системы, УП Частное предприятие Год основания: 2003 Количество сотрудников: 15 УНП: 690236997",
//            address = "220050, г. Минск, ул. Интернациональная, 11-123",
//            phones = "(017)203-51-50, (029)619-02-48",
//            email = "info@ibs.by",
//            website = "www.ibs.by"
//        )
//        databaseParent.push().setValue(corp5)
////
//        val corp6 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Бисмарт",
//            poster = "",
//description = "Бисмарт, ЗАО Закрытое акционерное общество Год основания: 1997 Количество сотрудников: 23 УНП: 101235837",
//            address = "220002, г. Минск, пр-т Машерова, 25-624",
//            phones = "(017)334-52-04",
//            email = "besmart@belabm.by",
//            website = "www.besmart.by"
//        )
//        databaseParent.push().setValue(corp6)
////
//        val corp7 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БИЭС-2",
//            poster = "",
//description = "БИЭС-2, СООООбщество с ограниченной ответственностью Год основания: 2007 Количество сотрудников: 8",
//            address = "220021, г. Минск, пер. Бехтерева, 10-1412",
//            phones = "(017)346-86-86",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp7)
////
//        val corp8 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БЛРсофт",
//            poster = "",
//description = "БЛРсофт, ИООО Иностранное предприятие Год основания: 2006 Количество сотрудников: 50 УНП: 190769928",
//            address = "220030, г. Минск, ул. Я. Купалы, 25-203",
//            phones = "(017)328-68-77",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp8)
////
//        val corp9 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Бомен-техно",
//            poster = "",
//description = "Бомен-техно, ООООбщество с ограниченной ответственностью Год основания: 1997 Количество сотрудников: 15 УНП: 101296316",
//            address = "220125, г. Минск, ул. Острошицкая, 12, оф. 2, комн. 1",
//            phones = "",
//            email = "info@obrabotka.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp9)
//////
//        val corp10 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Бондаренко Н. В.",
//            poster = "",
//description = "Бондаренко Н. В., ИП Предприниматель Год основания: 1997 Количество сотрудников: 1 УНП: 300544806",
//            address = "220024, г. Минск, ул. Кижеватова, 7/2-18",
//            phones = "(017)328-40-38, (029)646-46-38",
//            email = "bond_orsha@mail.ru",
//            website = ""
//        )
//        databaseParent.push().setValue(corp10)
//////
//        val corp11 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Брестстройцентр",
//            poster = "",
//description = "Брестстройцентр, ООО Общество с ограниченной ответственностью Год основания: 2006 Количество сотрудников: 5 УНП: 290478908",
//            address = "224013, г. Брест, б-р Шевченко, 6-302",
//            phones = "",
//            email = "",
//            website = ""
//        )
//        databaseParent.push().setValue(corp11)
//////
//        val corp12 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "БэстСофт",
//            poster = "",
//description = "БэстСофт, ИООО Иностранное предприятие Год основания: 2006 Количество сотрудников: 20 УНП: 690398000",
//            address = "220005, г. Минск, ул. Платонова, 22-505б",
//            phones = "",
//            email = "aliaksandr.drabo@psa-software.com",
//            website = "www.bestsoft.by"
//        )
//        databaseParent.push().setValue(corp12)
//////
//        val corp13 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "В Байком",
//            poster = "",
//description = "В Байком, ЧТУП Частное предприятие Год основания: 2009",
//            address = "220036, г. Минск, пер. Домашевский, 9-2",
//            phones = "",
//            email = "info@vb-by.com",
//            website = ""
//        )
//        databaseParent.push().setValue(corp13)
//////
//        val corp14 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Веб-студия Webcost",
//            poster = "",
//description = "Оказываем полный спектр в области разработки и продвижения сайтов в сети Интернет",
//            address = "",
//            phones = "+375292883880",
//            email = "http://webcost.by",
//            website = ""
//        )
//        databaseParent.push().setValue(corp14)
//////
//        val corp15 = CorporationDto(
//            idFirebase = databaseParent.key.toString(),
//            name = "Веб-студия Галиор",
//            poster = "http://belorussia.su/com_logo/1436961247logo1_big.jpg",
//description = "Разработка, создание и продвижение веб-сайтов и мобильных приложений, а также оказание услуг копирайтинга, перевод сайта на иностранные языки, разработка мультимедиа проектов.",
//            address = "г. Брест, ул. Советская, 80, (офис 11) и Минск",
//            phones = "+375 33 6075410, +375 44 4950188",
//            email = "info@galior.com",
//            website = "https://galior.com"
//        )
//        databaseParent.push().setValue(corp15)
  }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}