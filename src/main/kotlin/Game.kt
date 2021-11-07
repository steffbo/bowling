class Game {
    var frames = mutableListOf<Frame>()

    fun getFrameNumber(): Int = frames.size

    fun getNextFrameNumber(): Int = getFrameNumber() + 1

    fun isLastFrame(): Boolean = getFrameNumber() + 1 == MAX_FRAMES

    fun isOver(): Boolean = getFrameNumber() == MAX_FRAMES

    fun getScore(untilFrame: Int?): Int = frames
        .take(untilFrame ?: 10)
        .fold(0) { acc, frame -> acc + frame.getScore() }

    fun addFrame(frame: Frame) {
        updatePreviousFramesBonus(frame)
        frames.add(frame)
    }

    private fun updatePreviousFramesBonus(frame: Frame) {
        if (getFrameNumber() == 0) {
            return
        }

        // strike in last frame w/ last roll
        if (frame.roll3 == 10) {
            frame.addBonus(10)
        }

        val lastFrame = frames[getFrameNumber() - 1]
        if (lastFrame.isSpare() || lastFrame.isStrike()) {
            lastFrame.addBonus(frame.roll1)
        }

        if (frame.roll2 > 0 && lastFrame.isStrike()) {
            lastFrame.addBonus(frame.roll2)
            return
        }

        if (getFrameNumber() == 1) {
            return
        }

        val secondLastFrame = frames[getFrameNumber() - 2]
        if (secondLastFrame.isStrike() && lastFrame.isStrike()) {
            secondLastFrame.addBonus(frame.roll1)
        }
    }

    companion object {
        const val MAX_FRAMES = 10
    }
}
