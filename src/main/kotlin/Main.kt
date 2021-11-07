fun main() {
    val game = Game()
    println("👟 Welcome Bowling ace! 🎳")

    while (!game.isOver()) {
        val frameNumber = game.getNextFrameNumber()
        val roll1 = getScoreFromInput(frameNumber, 1)
        var roll2 = 0
        var roll3 = 0

        if (roll1 < 10 || game.isLastFrame()) {
            roll2 = getScoreFromInput(frameNumber, 2, roll1)
        }

        if (game.isLastFrame()) {
            roll3 = getScoreFromInput(frameNumber, 3)
        }

        game.addFrame(Frame(roll1, roll2, roll3))
        printGame(game)
    }

    println("Game ended! 🏆")
}

private fun getScoreFromInput(frameNumber: Int, rollNumber: Int, previousScore: Int = 0): Int {
    println("[Frame $frameNumber, Roll $rollNumber] Please enter score: ")

    val input = readln().toIntOrNull()
    // must be Int
    // must be > 0
    // must be <= 10
    // sum with previous must be <= 10
    // last frame can input multiple strikes
    if (input == null || input < 0 || input > 10 || (previousScore + input > 10 && frameNumber < 10)) {
        println("Invalid input")
        return getScoreFromInput(frameNumber, rollNumber, previousScore)
    }
    return input
}

private fun readln() = readLine()!!

private fun printGame(game: Game) {
    println("   1    2    3    4    5    6    7    8    9    10")
    println("⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽⎽")
    val sb = StringBuilder("⎸")
    (0..9).forEach { appendRoll(it, game, sb) }

    println(sb.replace(Regex("""10"""), "X"))
    sb.clear()
    sb.append("⎸")
    (0..9).forEach { appendScore(it, game, sb) }
    println(sb.toString())
    println("⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺⎺")
}

private fun appendRoll(idx: Int, game: Game, sb: StringBuilder) {
    val frame = getFrameForPrint(idx, game, sb) ?: return
    // many conditions for the last frame 🥲
    if (idx == 9 && (frame.isSpare() || frame.isStrike() || frame.roll2 + frame.roll3 == 10)) {
        // 5/X | 5/3 | 5/0
        if (frame.isSpare()) {
            sb.append("${frame.roll1}/${frame.roll3}⎹ ")
            return
        }
        // X5/
        if (frame.roll2 in 1..9 && frame.roll2 + frame.roll3 == 10) {
            sb.append("X${frame.roll2}/⎹ ")
            return
        }
        // XXX | X53
        sb.append("${frame.roll1}${frame.roll2}${frame.roll3}⎹ ")
        return
    }
    if (frame.isSpare()) {
        sb.append("${frame.roll1} /⎹ ")
        return
    }
    if (frame.isStrike()) {
        sb.append(String.format("%3s⎹ ", "X"))
        return
    }
    sb.append("${frame.roll1} ${frame.roll2}⎹ ")
}

private fun appendScore(idx: Int, game: Game, sb: StringBuilder) {
    getFrameForPrint(idx, game, sb) ?: return
    sb.append(String.format("%3s⎹ ", game.getScore(idx + 1)))
}

private fun getFrameForPrint(idx: Int, game: Game, sb: StringBuilder): Frame? {
    val frame = if (game.getFrameNumber() > idx) game.frames[idx] else null
    if (frame == null) {
        sb.append("   ⎹ ")
    }
    return frame
}
