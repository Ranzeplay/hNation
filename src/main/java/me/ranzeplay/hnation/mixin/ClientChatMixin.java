package me.ranzeplay.hnation.mixin;

import me.ranzeplay.hnation.client.CommunicationManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientChatMixin {
    @Inject(method = "sendChatMessage", cancellable = true, at = @At("HEAD"))
    public void sendMessage(String content, CallbackInfo ci) {
        CommunicationManager.handle(content);

        ci.cancel();
    }
}
