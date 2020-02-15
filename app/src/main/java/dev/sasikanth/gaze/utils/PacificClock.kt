package dev.sasikanth.gaze.utils

import org.threeten.bp.Clock
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

class PacificClock : Clock() {
  private val pacificZone = ZoneId.of(ZoneId.SHORT_IDS["PST"])
  private val clock = system(pacificZone)

  override fun withZone(zone: ZoneId?): Clock = clock.withZone(zone)

  override fun getZone(): ZoneId = clock.zone

  override fun instant(): Instant = clock.instant()
}
