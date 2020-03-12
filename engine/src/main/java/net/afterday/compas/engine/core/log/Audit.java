package net.afterday.compas.engine.core.log;

import net.afterday.compas.engine.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.engine.core.inventory.items.Item;

public class Audit {
    private static AuditImpl _auditImpl;

    public static void setAuditImpl(AuditImpl auditImpl) {
        _auditImpl = auditImpl;
    }

    public static void e(String message){
        _auditImpl.error(message);
    }

    public static void d(String message){
        _auditImpl.debug(message);
    }

    public static void logItemDropped(Item item) {
    }

    public static void logItemUsed(Item item) {
    }

    public static void logItemAdded(ItemAdded itemAdded) {
    }
}
