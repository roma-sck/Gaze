package dev.sasikanth.gaze.utils

import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

class TestClock(instant: Instant) : Clock() {

  constructor() : this(Instant.EPOCH)

  constructor(localDate: LocalDate, zoneOffset: ZoneOffset = ZoneOffset.UTC) : this(instantFromDateAtZone(localDate, zoneOffset))

  private var clock = fixed(instant, ZoneOffset.UTC)

  override fun withZone(zone: ZoneId): Clock = clock.withZone(ZoneOffset.UTC)

  override fun getZone(): ZoneId = clock.zone

  override fun instant(): Instant = clock.instant()

  fun advanceBy(duration: Duration) {
    clock = offset(clock, duration)
  }

  fun setDate(date: LocalDate, zone: ZoneId = this.zone) {
    val instant = instantFromDateAtZone(date, zone)
    clock = fixed(instant, zone)
  }
}

private fun instantFromDateAtZone(localDate: LocalDate, zone: ZoneId): Instant {
  return localDate.atStartOfDay(zone).toInstant()
}
