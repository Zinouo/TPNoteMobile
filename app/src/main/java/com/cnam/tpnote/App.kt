package com.cnam.tpnote

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class App : Application() {

    val database by lazy { QuizDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        // Prepopulate the database with categories and questions
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            clearDatabase()
            prepopulateDatabase()
            logDatabaseContents()
        }
    }

    private suspend fun clearDatabase() {
        val dao = database.quizDao()
        dao.clearAll()
    }

    private suspend fun prepopulateDatabase() {
        val dao = database.quizDao()

        // Check if the database is empty
        if (dao.getCategories().isEmpty()) {
            Log.d("App", "Prepopulating database")
            val categories = listOf(
                Category(name = "General Knowledge"),
                Category(name = "Science"),
                Category(name = "History"),
                Category(name = "Geography"),
                Category(name = "Gaming"),
                Category(name = "Hip Hop Music"),
                Category(name = "Elden Ring")
            )

            val categoryIds = categories.map { dao.insertCategory(it) }

            val questions = listOf(
                // General Knowledge
                Question(
                    text = "What is the capital of France?",
                    options = listOf("Berlin", "Madrid", "Paris", "Rome"),
                    correctAnswer = 2,
                    categoryId = categoryIds[0].toInt()
                ),
                Question(
                    text = "Which planet is known as the Red Planet?",
                    options = listOf("Earth", "Mars", "Jupiter", "Venus"),
                    correctAnswer = 1,
                    categoryId = categoryIds[0].toInt()
                ),
                Question(
                    text = "Who wrote 'Hamlet'?",
                    options = listOf("Charles Dickens", "J.K. Rowling", "William Shakespeare", "Mark Twain"),
                    correctAnswer = 2,
                    categoryId = categoryIds[0].toInt()
                ),
                Question(
                    text = "What is the smallest prime number?",
                    options = listOf("0", "1", "2", "3"),
                    correctAnswer = 2,
                    categoryId = categoryIds[0].toInt()
                ),
                Question(
                    text = "What is the largest ocean on Earth?",
                    options = listOf("Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"),
                    correctAnswer = 3,
                    categoryId = categoryIds[0].toInt()
                ),
                Question(
                    text = "What is the hardest natural substance on Earth?",
                    options = listOf("Gold", "Iron", "Diamond", "Platinum"),
                    correctAnswer = 2,
                    categoryId = categoryIds[0].toInt()
                ),

                // Science
                Question(
                    text = "What is the chemical symbol for water?",
                    options = listOf("H2O", "O2", "CO2", "H2"),
                    correctAnswer = 0,
                    categoryId = categoryIds[1].toInt()
                ),
                Question(
                    text = "What planet is known as the Red Planet?",
                    options = listOf("Earth", "Mars", "Jupiter", "Saturn"),
                    correctAnswer = 1,
                    categoryId = categoryIds[1].toInt()
                ),
                Question(
                    text = "What is the speed of light?",
                    options = listOf("299,792 km/s", "300,000 km/s", "150,000 km/s", "400,000 km/s"),
                    correctAnswer = 0,
                    categoryId = categoryIds[1].toInt()
                ),
                Question(
                    text = "Who developed the theory of relativity?",
                    options = listOf("Isaac Newton", "Albert Einstein", "Galileo Galilei", "Nikola Tesla"),
                    correctAnswer = 1,
                    categoryId = categoryIds[1].toInt()
                ),
                Question(
                    text = "What is the powerhouse of the cell?",
                    options = listOf("Nucleus", "Mitochondria", "Ribosome", "Chloroplast"),
                    correctAnswer = 1,
                    categoryId = categoryIds[1].toInt()
                ),
                Question(
                    text = "What gas do plants absorb from the atmosphere?",
                    options = listOf("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"),
                    correctAnswer = 2,
                    categoryId = categoryIds[1].toInt()
                ),

                // History
                Question(
                    text = "Who was the first President of the United States?",
                    options = listOf("Thomas Jefferson", "Abraham Lincoln", "George Washington", "John Adams"),
                    correctAnswer = 2,
                    categoryId = categoryIds[2].toInt()
                ),
                Question(
                    text = "What year did the Titanic sink?",
                    options = listOf("1905", "1912", "1918", "1923"),
                    correctAnswer = 1,
                    categoryId = categoryIds[2].toInt()
                ),
                Question(
                    text = "Who discovered America?",
                    options = listOf("Christopher Columbus", "Leif Erikson", "Amerigo Vespucci", "Ferdinand Magellan"),
                    correctAnswer = 0,
                    categoryId = categoryIds[2].toInt()
                ),
                Question(
                    text = "Which war was fought between the North and South regions in the United States?",
                    options = listOf("World War I", "World War II", "The Civil War", "The Revolutionary War"),
                    correctAnswer = 2,
                    categoryId = categoryIds[2].toInt()
                ),
                Question(
                    text = "Who was known as the Maid of Orleans?",
                    options = listOf("Joan of Arc", "Marie Antoinette", "Catherine the Great", "Queen Elizabeth I"),
                    correctAnswer = 0,
                    categoryId = categoryIds[2].toInt()
                ),
                Question(
                    text = "In which year did the Berlin Wall fall?",
                    options = listOf("1987", "1989", "1991", "1993"),
                    correctAnswer = 1,
                    categoryId = categoryIds[2].toInt()
                ),

                // Geography
                Question(
                    text = "What is the longest river in the world?",
                    options = listOf("Amazon River", "Nile River", "Yangtze River", "Mississippi River"),
                    correctAnswer = 1,
                    categoryId = categoryIds[3].toInt()
                ),
                Question(
                    text = "Which country has the largest population?",
                    options = listOf("India", "USA", "China", "Russia"),
                    correctAnswer = 2,
                    categoryId = categoryIds[3].toInt()
                ),
                Question(
                    text = "What is the capital of Australia?",
                    options = listOf("Sydney", "Melbourne", "Canberra", "Brisbane"),
                    correctAnswer = 2,
                    categoryId = categoryIds[3].toInt()
                ),
                Question(
                    text = "Which desert is the largest in the world?",
                    options = listOf("Sahara Desert", "Arabian Desert", "Gobi Desert", "Kalahari Desert"),
                    correctAnswer = 0,
                    categoryId = categoryIds[3].toInt()
                ),
                Question(
                    text = "Mount Everest is located in which mountain range?",
                    options = listOf("Andes", "Rockies", "Himalayas", "Alps"),
                    correctAnswer = 2,
                    categoryId = categoryIds[3].toInt()
                ),
                Question(
                    text = "What is the smallest country in the world?",
                    options = listOf("Monaco", "San Marino", "Vatican City", "Liechtenstein"),
                    correctAnswer = 2,
                    categoryId = categoryIds[3].toInt()
                ),

                // Gaming
                Question(
                    text = "What is the highest-grossing video game franchise of all time?",
                    options = listOf("Mario", "Pokemon", "Call of Duty", "FIFA"),
                    correctAnswer = 1,
                    categoryId = categoryIds[4].toInt()
                ),
                Question(
                    text = "What is the main character's name in the Legend of Zelda series?",
                    options = listOf("Zelda", "Link", "Ganon", "Epona"),
                    correctAnswer = 1,
                    categoryId = categoryIds[4].toInt()
                ),
                Question(
                    text = "Which company developed the game 'Fortnite'?",
                    options = listOf("EA Sports", "Epic Games", "Ubisoft", "Activision"),
                    correctAnswer = 1,
                    categoryId = categoryIds[4].toInt()
                ),
                Question(
                    text = "What is the best-selling game console of all time?",
                    options = listOf("PlayStation 2", "Xbox 360", "Nintendo Switch", "Wii"),
                    correctAnswer = 0,
                    categoryId = categoryIds[4].toInt()
                ),
                Question(
                    text = "In which game do players compete in a battle royale to be the last one standing?",
                    options = listOf("Minecraft", "Overwatch", "Fortnite", "League of Legends"),
                    correctAnswer = 2,
                    categoryId = categoryIds[4].toInt()
                ),
                Question(
                    text = "What is the name of the city in Grand Theft Auto V?",
                    options = listOf("Liberty City", "Vice City", "Los Santos", "San Fierro"),
                    correctAnswer = 2,
                    categoryId = categoryIds[4].toInt()
                ),

                // Hip Hop Music
                Question(
                    text = "Who is known as the 'King of Rap'?",
                    options = listOf("Tupac", "Jay-Z", "Eminem", "Nas"),
                    correctAnswer = 2,
                    categoryId = categoryIds[5].toInt()
                ),
                Question(
                    text = "Which artist released the album 'To Pimp a Butterfly'?",
                    options = listOf("Kendrick Lamar", "J. Cole", "Drake", "Kanye West"),
                    correctAnswer = 0,
                    categoryId = categoryIds[5].toInt()
                ),
                Question(
                    text = "Who is the founder of Aftermath Entertainment?",
                    options = listOf("Dr. Dre", "Ice Cube", "Snoop Dogg", "Eazy-E"),
                    correctAnswer = 0,
                    categoryId = categoryIds[5].toInt()
                ),
                Question(
                    text = "Which rapper is known for the song 'Sicko Mode'?",
                    options = listOf("Travis Scott", "Lil Wayne", "Post Malone", "Drake"),
                    correctAnswer = 0,
                    categoryId = categoryIds[5].toInt()
                ),
                Question(
                    text = "Which artist released the hit single 'Hotline Bling'?",
                    options = listOf("J. Cole", "Drake", "Kanye West", "Future"),
                    correctAnswer = 1,
                    categoryId = categoryIds[5].toInt()
                ),
                Question(
                    text = "Who is the artist behind the album 'Astroworld'?",
                    options = listOf("Travis Scott", "Juice WRLD", "Trippie Redd", "Lil Uzi Vert"),
                    correctAnswer = 0,
                    categoryId = categoryIds[5].toInt()
                ),

                // Elden Ring
                Question(
                    text = "Who is the main antagonist in Elden Ring?",
                    options = listOf("Radagon", "Malenia", "Rennala", "Godfrey"),
                    correctAnswer = 1,
                    categoryId = categoryIds[6].toInt()
                ),
                Question(
                    text = "What is the primary currency used in Elden Ring?",
                    options = listOf("Souls", "Runes", "Blood Echoes", "Mana"),
                    correctAnswer = 1,
                    categoryId = categoryIds[6].toInt()
                ),
                Question(
                    text = "Who is the final boss in Elden Ring?",
                    options = listOf("Godfrey", "Radagon", "Elden Beast", "Malenia"),
                    correctAnswer = 2,
                    categoryId = categoryIds[6].toInt()
                ),
                Question(
                    text = "Which company developed Elden Ring?",
                    options = listOf("Bethesda", "CD Projekt Red", "FromSoftware", "Ubisoft"),
                    correctAnswer = 2,
                    categoryId = categoryIds[6].toInt()
                ),
                Question(
                    text = "Which character is known as the 'Blade of Miquella'?",
                    options = listOf("Rennala", "Ranni", "Radahn", "Malenia"),
                    correctAnswer = 3,
                    categoryId = categoryIds[6].toInt()
                ),
                Question(
                    text = "What is the name of the starting area in Elden Ring?",
                    options = listOf("Limgrave", "Caelid", "Liurnia", "Altus Plateau"),
                    correctAnswer = 0,
                    categoryId = categoryIds[6].toInt()
                )
            )

            questions.forEach { dao.insertQuestion(it) }
        } else {
            Log.d("App", "Database already prepopulated")
        }
    }

    private suspend fun logDatabaseContents() {
        val dao = database.quizDao()
        val categories = dao.getCategories()
        val questions = dao.getAllQuestions()

        Log.d("App", "Categories in DB: ${categories.size}")
        categories.forEach { category ->
            Log.d("App", "Category: ${category.name}")
        }

        Log.d("App", "Questions in DB: ${questions.size}")
        questions.forEach { question ->
            Log.d("App", "Question: ${question.text}, Options: ${question.options.joinToString()}")
        }
    }
}
