@file:JvmName("Main")

package archives.tater.bot.verifier

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Member
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

val dotenv: Dotenv = Dotenv.load()

val DELAY = dotenv["DELAY"].toInt().seconds

val ROLE = Snowflake(dotenv["ROLE_ID"])

fun Member.display() = "$effectiveName ($id)"

val firstSeen = mutableSetOf<Snowflake>()

suspend fun delayedVerify(member: Member) {
    delay(DELAY)
    firstSeen.remove(member.id)
    member.fetchMemberOrNull()?.run {
        addRole(ROLE)
        println("${member.display()} verified")
    } ?: run {
        println("${member.display()} no longer exists")
    }
}

suspend fun main() {

    with (Kord(dotenv["BOT_TOKEN"])) {
        on<MessageCreateEvent> {
            val member = member
            if (member != null && ROLE !in member.roleIds && member.id !in firstSeen) {
                println("Saw first message from ${member.display()}")
                firstSeen.add(member.id)
                delayedVerify(member)
            }
        }

        on<ReadyEvent> {
            println("Logged in!")
        }

        login {}
    }
}
