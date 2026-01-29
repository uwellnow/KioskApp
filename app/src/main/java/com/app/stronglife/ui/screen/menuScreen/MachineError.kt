package com.app.stronglife.ui.screen.menuScreen

sealed class MachineError {
    object WaterShortage : MachineError()
    object CupShortage : MachineError()
    data class OtherError(val code: Int) : MachineError()
    object SerialError : MachineError()
}
