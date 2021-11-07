class Frame(
    val roll1: Int,
    val roll2: Int,
    val roll3: Int,
    private var bonus: Int = 0,
) {
    fun isSpare() = !isStrike() && (roll1 + roll2 == 10)

    fun isStrike() = roll1 == 10

    fun getScore() = roll1 + roll2 + roll3 + bonus

    fun addBonus(bonus: Int) {
        this.bonus += bonus
    }
}
