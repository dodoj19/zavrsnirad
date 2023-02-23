package com.example.zavrsnirad

import com.patrykandpatrick.vico.core.entry.ChartEntry
import java.time.LocalDate

class Entry(
    val localDate: LocalDate,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = Entry(localDate, x, y)
}