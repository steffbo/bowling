import spock.lang.Specification

class GameSpec extends Specification {

    Game game

    def setup() {
        game = new Game()
    }

    def "addFrame adds a new frame to the game"() {
        given:
        Frame frame = new Frame(10, 0, 0, 0)

        when:
        game.addFrame(frame)

        then:
        game.frames.size() == 1
    }

    def "getNextFrameNumber returns 1 for a new Game"() {
        expect:
        game.getNextFrameNumber() == 1
    }

    def "getNextFrameNumber returns 4 after 3 frames were added to a Game"() {
        given:
        3.times {
            game.addFrame(new Frame(0, 0, 0, 0))
        }

        expect:
        game.getNextFrameNumber() == 4
    }

    def "getTotalScore after 10 frames without special frames"() {
        when:
        10.times {
            game.addFrame(new Frame(it, 0, 0, 0))
        }

        then:
        game.getScore() == 45
    }

    def "getTotalScore after 3 strikes"() {
        when:
        3.times {
            game.addFrame(new Frame(10, 0, 0, 0))
        }

        then:
        game.getScore() == 60
    }

    def "getTotalScore after 11 spares"() {
        when:
        9.times {
            game.addFrame(new Frame(5, 5, 0, 0))
        }
        game.addFrame(new Frame(5, 5, 5, 0))

        then:
        game.getScore() == 150
    }

    def "getTotalScore for perfect game (12 strikes)"() {
        when:
        9.times {
            game.addFrame(new Frame(10, 0, 0, 0))
        }
        game.addFrame(new Frame(10, 10, 10, 0))

        then:
        game.getScore() == 300
    }

    def "getTotalScore for average game with all in between scores"() {
        when: '5 + 4'
        game.addFrame(new Frame(5, 4, 0, 0))

        then: '+9'
        game.getScore() == 9

        when: '3 + 7'
        game.addFrame(new Frame(3, 7, 0, 0))

        then: '+10'
        game.getScore() == 19

        when: '2 + 4'
        game.addFrame(new Frame(2, 4, 0, 0))

        then: '+6 & bonus 2'
        game.getScore() == 27

        when: '9 + 1'
        game.addFrame(new Frame(9, 1, 0, 0))

        then: '+10'
        game.getScore() == 37

        when: 'strike'
        game.addFrame(new Frame(10, 0, 0, 0))

        then: '+10 & bonus 10'
        game.getScore() == 57

        when: 'strike'
        game.addFrame(new Frame(10, 0, 0, 0))

        then: '+10 & bonus 10'
        game.getScore() == 77

        when: '5 + 3'
        game.addFrame(new Frame(5, 3, 0, 0))

        then: '+8 & bonus 5 & bonus 3'
        game.getScore() == 93

        when: '0 + 0'
        game.addFrame(new Frame(0, 0, 0, 0))

        then: '+0'
        game.getScore() == 93

        when: 'spare + strike'
        game.addFrame(new Frame(5, 5, 10, 0))

        then: '+20 & bonus 10'
        game.getScore() == 123
    }
}
