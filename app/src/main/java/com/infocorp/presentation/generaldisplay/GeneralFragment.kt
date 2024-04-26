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

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw Exception()

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

        // проверка dadata
//        GlobalScope.launch(Dispatchers.Default) {
//           val result = CorporationFactory().corporationService.getAllCorporations("АБУШЕНКО ВИ")
//            Log.i("MyLog","${result.body()?.suggestions}")
//        }


        val database = Firebase.database.getReference("CORPORATION")


//        val corp1 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Альфа-Гомель",
//            poster = "http://belorussia.su/com_logo/1422426178logo1_big.jpg",
//            description = "Компания «Альфа» предлагает Вашему вниманию весь спектр услуг в сфере программного обеспечения!Для  автоматизации документооборота предприятий нашими специалистами разработан комплекс готовых программ Альфа-Офис.Кроме того в сфере информационных технологий мы можем предложить комплексную защиту от воздействия вредоносных программ (ESET, лаборатория Касперского, Dr.Web, VBA32); разработка и/или доработка программного обеспечения; сопровождение программных продуктов; разработка, продвижение и сопровождение Web-сайтов; консультирование в области организации автоматизированного учета; поставка и настройка программных продуктов сторонних разработчиков (  ПО Microsoft, графическо-го ПО и многих других).Также мы предоставляем услуги по установке, настройке и сопровождению телекоммуникационных систем, мини АТС (запись разговоров, оптимизация расходов между филиалами одного предприятия, сохранение номера при переезде в новое помещение,   снижение затрат на связь, детализация звонков, интеграция с CRM, голосовое меню, группы вызова, безопасность, видеозвонки, конференцсвязь и другое).",
//            address = "246000, Республика Беларусь, г.Гомель, ул. Федюнинского 17",
//            phones = "+375 (232) 68-27-39, 68-26-44,+375(29)168-27-39, +375(33)682-74-97, +375(25)603-27-44",
//            email = "sales@alfagomel.by",
//            website = "http://alfagomel.by"
//        )
//        val corp2 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "1С-Минск",
//            description = "Компания СООО «1С-Минск» занимается автоматизацией управления и учета на базе программных продуктов «1С». СООО «1С-Минск» — совместное предприятие Фирмы «1С» (г. Москва)",
//            address = "г. Минск, ул. Шафарнянская, д.11, офис 104",
//            phones = "+375-17-360-85-50",
//            email = "office@1c-minsk.by",
//            website = "www.1c-minsk.by"
//        )
//        val corp3 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "4Д",
//            description = "4Д, ОДООбщество с дополнительной ответственностью. Год основания: 2008. Количество сотрудников: 6. УНП: 590830939",
//            address = "230023, г. Гродно, ул. 17 Сентября, 49-309, 310",
//            phones = "(029)189-58-88"
//        )
//        val corp4 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "7 Версия",
//            poster = "http://belorussia.su/com_logo/1428050869logo1_big.jpg",
//            description = "Любой бизнес постоянно находится в поисках целевых клиентов для продажи товаров и услуг. Все знают, что клиентов можно получить из интернета, но не все знают, как это сделать.  Компании платят за услуги по разработке сайтов, лендингов, за интернет-рекламу, за SEO. В большинстве случаев речь идет об оплате процессов, а не о конкретных результатах. Агентства интернет-рекламы не гарантируют продажи, равно как их не гарантируют разработчики сайтов и компании, занимающиеся поисковым продвижением. Мы работаем по модели, при которой Заказчики оплачивают только качественные целевые обращения, при этом не несут расходов на создание рекламных страниц, сплит-тестирование и рекламные кампании. Наша схема работы подробно описана на сайте http://7v.by. Компания работает на рынках Беларуси, России, Израиля, Евросоюза и в странах Северной Америки. Для рынка Беларуси наше предложение абсолютно уникально.",
//            address = "220113, г. Минск, ул. Мележа, 1, (оф. 1107)",
//            phones = "+375173928206",
//            email = "info@7version.com",
//            website = "http://7v.by"
//        )
//        val corp5 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Alfa Project",
//            poster = "http://belorussia.su/com_logo/1459762521logo1_big.jpg",
//            description = "Создание сайтов, оптимизация и техническая поддержка. Размещение рекламы на билбордах и ситилайтах. Изготовление наружной рекламы (баннеры, световые короба, объёмные буквы и прочее...). Разработка макетов, логотипов и фирменного стиля компании.",
//            address = "231300, Гродненская область, г. Лида, ул. Мицкевича 11-7",
//            phones = "+375 33 351-63-54",
//            email = "info@alfa-project.by",
//            website = "alfa-project.by"
//        )
//        val corp6 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Altoros Development",
//            poster = "http://belorussia.su/com_logo/1349680225logo1_big.jpg",
//            description = "Altoros Development – специалист в разработке продуктов под заказ для технологических компаний-производителей программного обеспечения и стартапов. Одним из приоритетных направлений компании является исследовательская работа – Research & Development. R&D-фокус дает возможность в числе первых использовать в проектах новинки как от ведущих компаний – Adobe, Microsoft, Oracle, Google, Apple – так и игроков, только формирующих новые тренды в отрасли. Как результат, сегодня Altoros является крупнейшим разработчиком на Ruby on Rails в Беларуси, а также участвует в создании продуктовых решений от мировых лидеров в сферах Cloud Computing и NoSQL. Основанная в 2001 году, компания работает с заказчиками из стран Северной Америки и Европы. За это время было реализовано более 500 проектов. Подразделения Altoros расположены в Беларуси, США, Норвегии, Дании,Великобритании и Аргентине. В компании работает более 220 человек. 90% технических специалистов – высококвалифицированные сотрудники Middle, Senior и Lead уровня со средним опытом в софтверной разработке более 7 лет. Altoros Development активно поддерживает белорусские сообщества разработчиков и является компанией-резидентом Парка высоких технологий с 2008 года. Подробнее о компании – на сайте www.altoros.com.",
//            phones = "+375(29)3670849, +375(17)3880132",
//            email = "cv@altoros.com",
//            website = "http://altoros.com"
//        )
//        val corp7 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "Appside Digital Agency",
//            poster = "http://belorussia.su/com_logo/1517501359logo1_big.jpg",
//            description = "Разработка и продвижение эффективных и качественных сайтов и веб-приложений в Беларуси. Разработка сайтов на WordPress и Laravel",
//            phones = "+375447547950",
//            email = "http://appside.by",
//        )
//        val corp8 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "BPM Cloud",
//            poster = "http://belorussia.su/com_logo/1626874262logo1_big.jpg",
//            description = "Digital-агентство «BPM Cloud» – это комплексный подход к интернет-маркетингу. Под знаменем компании собрались специалисты различных направлений, что позволяет с успехом справляться даже с проектами повышенной сложности. Мы работаем с представителями сферы услуг, риелторами, строителями и т.д. У нас накоплен бесценный опыт, который мы готовы использовать на Ваше благо. Создание сайта – только первый шаг в онлайн-мире. Важно организовать действенное продвижение продукта в Глобальной сети. Предоставляем услуги бизнесу так, чтобы был ощутим осязаемый результат. Никогда не подходим шаблонно, что позволяет добиваться хороших показателей.",
//            address = "Минск, пр-т Независимости, дом 77, офис 53",
//            phones = "+375 (29) 32-44-000",
//            email = "info@bpm-cloud.by",
//            website = "https://bpm-cloud.by"
//        )
//        val corp9 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "BrendApp",
//            poster = "http://belorussia.su/com_logo/1427206443logo1_big.jpg",
//            description = "Разработка мобильных приложений для компаний в сфере ремонта и строительства",
//            phones = "+375(29)650-02-75",
//            email = "info@brend-app.by",
//            website = "http://brend-app.by"
//        )
//        val corp10 = CorporationDto(
//            idFirebase = database.key.toString(),
//            name = "ChatLabs",
//            poster = "http://belorussia.su/com_logo/1680886291logo1_big.jpg",
//            description = "МЫ — СТУДИЯ РАЗРАБОТКИ ЧАТ-БОТОВ, Наша миссия — освободить ваш бизнес от рутинных задач. Самый востребованный язык программирования. Система хранения и управления данными. Надежный сервер. Платформа для распознавания естественного языка.",
//            address = "Минск, Пр-т Дзержинского, 131",
//            phones = "+375(44) 598-85-87",
//            email = "https://chatlabs.ru"
//        )
//
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

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children
                result.forEach {
                    val corp = it.getValue(CorporationDto::class.java)
                    Log.i("MyLog", "$corp")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}