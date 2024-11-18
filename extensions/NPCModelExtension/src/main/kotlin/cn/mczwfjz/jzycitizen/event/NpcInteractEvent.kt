package cn.mczwfjz.jzycitizen.event

import cn.mczwfjz.event.InteractModelEvent
import cn.mczwfjz.jzycitizen.entity.ReferenceNpcEntry
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import org.bukkit.entity.Player


@Entry("on_npc_interact", "When a player clicks on an NPC", Colors.YELLOW, "fa6-solid:people-robbery")
class NpcInteractEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The identifier of the NPC.")
    // The NPC that needs to be interacted with.
    val identifier: Ref<ReferenceNpcEntry> = emptyRef(),
) : EventEntry

private fun onReferenceNpcInteract(player: Player, id: String, query: Query<NpcInteractEventEntry>) {
    val references: Sequence<ReferenceNpcEntry> = Query findWhere { it.npcId == id }
    val identifiers = references.map { it.id }.toList()
    val findWhere = query.findWhere {
        it.identifier.id in identifiers
    }
    findWhere startDialogueWithOrNextDialogue player
}

@EntryListener(NpcInteractEventEntry::class)
fun onNpcClick(event: InteractModelEvent, query: Query<NpcInteractEventEntry>) {
    onReferenceNpcInteract(event.player, event.id, query)
}

