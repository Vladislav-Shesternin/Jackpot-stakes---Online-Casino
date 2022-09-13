package spintimez.slot.com.util

class Once {

    private var event = Event.NOT_WAS



    fun once(block: () -> Unit) {
        if (event == Event.NOT_WAS) {
            event = Event.WAS
            block()
        }
    }

    fun reset(){
        event = Event.NOT_WAS
    }



    enum class Event {
        WAS, NOT_WAS
    }

}