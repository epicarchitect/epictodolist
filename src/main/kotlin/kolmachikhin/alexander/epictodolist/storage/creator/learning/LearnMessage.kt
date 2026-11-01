package kolmachikhin.alexander.epictodolist.storage.creator.learning

class LearnMessage(
    val id: Int,
    val message: String,
    val questions: Array<String>,
    private val answers: Array<String>
) {
    fun getAnswer(i: Int) = if (i >= 0 && i < answers.size) answers[i] else "..."

    fun getQuestion(i: Int) = if (i >= 0 && i < questions.size) questions[i] else "..."
}