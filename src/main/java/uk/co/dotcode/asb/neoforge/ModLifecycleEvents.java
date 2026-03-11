package uk.co.dotcode.asb.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ModLifecycleEvents {

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        // do load-complete logic here
    }
}