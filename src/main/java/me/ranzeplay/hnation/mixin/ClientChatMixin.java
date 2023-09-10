package me.ranzeplay.hnation.mixin;

import me.ranzeplay.hnation.features.communication.ClientCommunicationHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientChatMixin {
    @Inject(method = "sendChatMessage", cancellable = true, at = @At("HEAD"))
    public void sendMessage(String content, CallbackInfo ci) {
        ClientCommunicationHandler.handle(content);

        ci.cancel();
    }
}
