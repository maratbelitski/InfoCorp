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
//            name = "Robosoft",
//            poster = "http://belorussia.su/com_logo/1371724587logo1_big.jpg",
//            description = "Robosoft LLC - успешная транснациональная компания, на рынке 7 лет, занимаемся предоставлением услуг по разработке и поддержке комплексных IT решений. Деятельность компании осуществляется параллельно в двух направлениях - системы GPS мониторинга и решения для финансовых рынков. Robosoft — это сплоченная команда единомышленников, нацеленных на  качественный результат. Наши сильные стороны - высокий профессионализм, интересные проекты, корпоративная культура и материальная забота о своих сотрудниках.",
//            phones = "(056) 788-91-62",
//            email = "hr@robosoft.info",
//            website = "http://www.robosoft.info"
//        )
//        val corp2 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "SaM Solutions Belarus",
//            description = "SaM Solutions Belarus, ИЧУНПП. Иностранное предприятие. Год основания: 2001. Количество сотрудников: 340. УНП: 800008653",
//            address = "220037, г. Минск, ул. Филимонова, 15",
//            phones = "(017)309-17-09",
//            email = "info@sam-solutions.net",
//            website = "www.sam-solutions.ru"
//        )
//        val corp3 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Seodev.by",
//            poster = "http://belorussia.su/com_logo/1582409817logo1_big.jpg",
//            description = "SEODEV.by – рекламное агентство, специализирующееся на SEO продвижении сайтов, контекстной рекламе в интернете, разработке сайтов, SMM-продвижении. Наша команда находится в Беларуси (Минск), однако мы сотрудничаем с компаниями по всему миру. У нас более 5 лет опыта в продвижении бизнеса, более 100 проектов по SEO, контекстной рекламе, SMM. Наши клиенты доверяют нашей экспертности!",
//            address = "Минск, ул. Карла Либкнехта 66А, 220004",
//            phones = "+375257778177",
//            email = "info@seodev.by",
//            website = "https://seodev.by"
//        )
//        val corp4 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Site Developer",
//            poster = "http://belorussia.su/com_logo/1470327290logo1_big.jpg",
//            description = "Разработка, продвижение, техническая поддержка и контекстная реклама Вашего WEB-Сайта. Мы - команда профессиональных дизайнеров, веб-программистов, маркетологов и СЕО-специалистов. Всегда рады придти на помощь в продвижении Вашего бизнеса",
//            address = "г. Минск, Мележа, 1",
//            phones = "+375-29 11-55-010, +375-33 629-64-56",
//            email = "ip.senkevich@gmail.com",
//            website = "http://sitedeveloper.by"
//        )
//        val corp5 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "SkySoft",
//            poster = "http://belorussia.su/com_logo/1607188026logo1_big.jpg",
//            description = "Занимаемся разработкой сайтов с индивидуальным дизайном и внедрением CRM",
//            address = "Лещинского 8/5,оф 401",
//            phones = "+375297678871",
//            email = "info@sky-soft.su",
//            website = "http://sky-soft.su"
//        )
//        val corp6 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Straut & Skobelev",
//            poster = "http://belorussia.su/com_logo/1583509477logo1_big.jpg",
//            description = "Делаем сайты и интернет магазины своим любимым заказчикам уже 3 года. Вы тоже можете стать нашим заказчиком, мы заказчиков любим, ценим и следим за их здоровьем в интернете&#128522.",
//            address = "Минск, Куйбышева 22",
//            phones = "+375 (25) 731-72-92",
//            email = "connect@straut-skobelev.com",
//            website = "https://straut-skobelev.com"
//        )
//        val corp7 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "VISUTECH SYSTEM LTD",
//            phones = "+375445811316",
//            email = "info@kksdc.com",
//            website = "http://kksdc.com"
//        )
//        val corp8 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Line-Landing",
//            poster = "http://belorussia.su/com_logo/1528747232logo1_big.jpg",
//            description = "Компания Line-Landing предлагает вам разработку и продвижение сайта.",
//            address = "г. Гродно, ул. Мостовая 31, 3 этаж",
//            phones = "+375 29 285 84 46",
//            email = "line-xweb@gmail.com",
//            website = "http://line-landing.by"
//        )
//        val corp9 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "NEW LEVEL",
//            poster = "http://belorussia.su/com_logo/1456847900logo1_big.jpg",
//            description = "Студия продающих сайтов. Веб-студия  «NEW LEVEL» оказывает услуги по созданию и продвижению продающих сайтов. Превращая интернет – ресурс в эффективный бизнес-инструмент, наша команда создает уникальный адаптивный дизайн сайта, максимально удобный для пользователя функционал, а также проводит полную оптимизацию веб-ресурса. Мы работаем над созданием сайтов любой тематики, сложности и оптимизируем их под  различный формат экрана. Хотите получить быстрый  продающий веб-ресурс? Обращайтесь к нам. Мы знаем все секреты продающих сайтов.",
//            address = "г. Брест ул. Ясеневая 8/1",
//            phones = "+375295208595",
//            email = "info@new-level.by",
//            website = "http://new-level.by"
//        )
//        val corp10 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "OmegaSoftware (Омегасофтвер)",
//            description = "OmegaSoftware GmbH (Омегасофтвер), ИПИностранное предприятие. Год основания: 1992. Количество сотрудников: 72. УНП: 101179436",
//            address = "220123, г. Минск, ул. В. Хоружей, 29-55",
//            phones = "(017)334-42-62-рук., 334-41-21, 334-42-73",
//            email = "omega@omegasoftware.eu",
//            website = "www.omegasoftware.ru"
//        )

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