import java.io.File
import java.io.InputStream
import java.io.FileWriter as FileWriter


var listaStref = arrayListOf<Strefa>()

var listaTerminow = arrayListOf<Termine>()
var listaNrTerminow = arrayListOf<String>()

var listaTerminowPersonalnych = arrayListOf<PersonalPlanung>()
var listaNrTerminowPersonalnych = arrayListOf<String>()

var checker: Boolean = false

val nazwaPliku = "test.txt"

fun main() {


    var numerwiersza = 10
    while (numerwiersza != 9) {
        println("Wybierz opcje")
        println("1 :wygeneruj liste")
        println("2 :zapisz do pliku")
        println("3 :wczytaj plik")
        println("4 :wyswietl liste")
        println("5 :xmlTerminy")
        println("6 :xmlPersonalPlannung")
        println("7 :załaduj Xml")
        println("8 :pokaz Plan Indywidualny")
        
        

        println("9 :wyjscie")
        print("jaką opcje wybierasz:")
        numerwiersza = readLine()!!.toInt()
        if (numerwiersza == 1) genTestowy(50)
        if (numerwiersza == 2) zapiszDoPliku(nazwaPliku)
        if (numerwiersza == 3) wczytajPlik(nazwaPliku)
        if (numerwiersza == 4) wyswietlListeStref()
        if (numerwiersza == 5) XmlReaderTerminy()
        if (numerwiersza == 6) XmlReaderPersonalPlannung()
        if (numerwiersza == 7) {
            XmlReaderPersonalPlannung() 
            XmlReaderTerminy()
        }
        if (numerwiersza == 8) PlanIndywidualny()

        if (numerwiersza == 9) {
            var potwierdzenie = "N"
            while (potwierdzenie != "y") {

                println("czy napewno chcesz wyjsc? Y/N")
                potwierdzenie = readLine()!!
                if (potwierdzenie == "y") numerwiersza = 9
            }
        }

    }


}

fun PlanIndywidualny() {
   println("Podaj Imie i Nazwisko")
    var filrt = readLine().toString()
    var ListaTerminowPrzefiltronachych = arrayListOf<String>()
    var Plan = ArrayList<Termine>()
    listaTerminowPersonalnych.forEach {

        if (it.NameVoll.contains(filrt)) {
            if (ListaTerminowPrzefiltronachych.contains(it.TerminNr)) {
            } else
                ListaTerminowPrzefiltronachych.add(it.TerminNr)
        }


    }

        ListaTerminowPrzefiltronachych.forEach { filrt ->
            listaTerminow.forEach { baza ->
                if (baza.TermineNr.contains(filrt)) Plan.add(baza)
            }
        }
        Plan.forEach{
            println()
            println(it.Datum)
            println(    it.Kunde)
            println("   ${it.Ort}  ${it.PLZ}")
            println("   ${it.Strasse} ")
            }






        //ListaTerminowPrzefiltronachych.forEach {println(it)}
        println("Lista pozycji: ${ListaTerminowPrzefiltronachych.size}")
    ListaTerminowPrzefiltronachych.clear()
    Plan.clear()

}


fun zapiszDoPliku(nazwaPliku: String) {
    val fileWriter = FileWriter(nazwaPliku)
    for (i in 0 until listaStref.size) {
        fileWriter.write("${listaStref[i].numer};${listaStref[i].nazwa};${listaStref[i].dlugosc};${listaStref[i].opis};\n")
    }
    fileWriter.close()
}

fun wczytajPlik(nazwaPliku: String) {
    try {
        listaStref.clear()

        val liniePliku = File(nazwaPliku).readLines()
        liniePliku.forEach {
            val linia = it.split(";") as ArrayList<String>
            listaStref.add(Strefa(linia[0].toInt(), linia[1], linia[2], linia[3]))
        }
    } catch (ex: Exception) {
        print(ex.message)
    }
}

fun wyswietlListeStref() {
    listaStref.forEach { println("${it.numer} ${it.nazwa} ${it.dlugosc} ${it.opis}") }
}


data class Strefa(
    var numer: Int,
    var nazwa: String,
    var dlugosc: String,
    var opis: String
)

fun genTestowy(ilosc: Int) {
    listaStref.clear()
    for (i in 0 until ilosc) {
        val num = i + 1
        listaStref.add(Strefa(num, "nazwa$num", "dlugosc$num", "opis$num"))
    }
}

fun XmlReaderTerminy() {
    try {
        var i = 0
        val inputStream: InputStream = File("import.xml").inputStream()
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach {

                var text = it.toString()
                var termin = it.toString()
                if (text.contains("</TerminNr>")) {

                    termin = termin.removePrefix("      <TerminNr>")
                    termin = termin.removeSuffix("</TerminNr>")
                    if (listaNrTerminow.contains(termin)) {
                    } else {
                        listaTerminow.add(Termine(termin, ""))
                        listaNrTerminow.add(termin)
                    }
                }
                if (text.contains("<Kunde>")) {
                    termin = termin.removePrefix("      <Kunde>").removeSuffix("</Kunde>")
                    listaTerminow.last().Kunde = termin
                }

                if (listaNrTerminow.size != 0) {
                    if (text.contains("<Ort>")) {
                        termin = termin.removePrefix("      <Ort>").removeSuffix("</Ort>")
                        listaTerminow.last().Ort = termin
                    }
                }

                if (text.contains("<PLZ>")) {
                    termin = termin.removePrefix("      <PLZ>").removeSuffix("</PLZ>")
                    listaTerminow.last().PLZ = termin.toInt()
                }

                if (text.contains("<Strasse>")) {
                    termin = termin.removePrefix("      <Strasse>").removeSuffix("</Strasse>")
                    listaTerminow.last().Strasse = termin
                }

                if (text.contains("<Datum>")) {
                    termin = termin.removePrefix("      <Datum>").removeSuffix("</Datum>")
                    termin = termin.removeRange(10 until termin.length)
                    listaTerminow.last().Datum = termin
                }
            }


        }
    } catch (ex: Exception) {
        println(ex.message)
    }

//    7
}

fun XmlReaderPersonalPlannung() {
    try {
        val inputStream: InputStream = File("import.xml").inputStream()
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach {


                val text = it.toString()
                var termin = it.toString()
                if (text.contains("<Personalplanung")) {
                    checker = true
                }

                if (checker) {
                    if (text.contains("</TerminNr>")) {
                        termin = termin.removePrefix("      <TerminNr>").removeSuffix("</TerminNr>")
                        listaTerminowPersonalnych.add(PersonalPlanung(termin, ""))
                    }
                    if (text.contains("<NameVoll>")) {
                        termin = termin.removePrefix("      <NameVoll>").removeSuffix("</NameVoll>")
                        listaTerminowPersonalnych.last().NameVoll = termin
                    }
                }

            }
        }

    } catch (ex: Exception) {
        println(ex.message)
    }

//    listaTerminowPersonalnych.forEach {
//        println(it)
//    }

}


