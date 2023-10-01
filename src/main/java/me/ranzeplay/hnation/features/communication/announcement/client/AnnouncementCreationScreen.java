package me.ranzeplay.hnation.features.communication.announcement.client;

import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class AnnouncementCreationScreen extends Screen {
    protected AnnouncementCreationScreen() {
        super(Text.literal("Create a new announcement"));
    }

    TextFieldWidget titleTextField;
    EditBoxWidget contentTextField;

    @Override
    protected void init() {
        var mainGrid = new GridWidget().setSpacing(4);
        mainGrid.getMainPositioner().alignHorizontalCenter().margin(4);
        var mainAdder = mainGrid.createAdder(1);
        mainAdder.add(new TextWidget(this.title, this.textRenderer).alignCenter());

        var titleGrid = new GridWidget();
        titleGrid.getMainPositioner().alignLeft().margin(2);
        var titleAdder = titleGrid.createAdder(1);
        titleAdder.add(new TextWidget(Text.literal("Title"), this.textRenderer).alignLeft());
        titleTextField = new TextFieldWidget(this.textRenderer, 10, 10, width - 20, 20, Text.empty());
        titleTextField.setPlaceholder(Text.literal("Title").formatted(Formatting.GRAY));
        titleAdder.add(titleTextField);
        titleGrid.refreshPositions();
        mainAdder.add(titleGrid);

        var contentGrid = new GridWidget();
        contentGrid.getMainPositioner().alignLeft().margin(2);
        var contentAdder = contentGrid.createAdder(1);
        contentAdder.add(new TextWidget(Text.literal("Content"), this.textRenderer).alignLeft());
        contentTextField = new EditBoxWidget(this.textRenderer, 10, 50, width - 20, height - 150, Text.literal("Content").formatted(Formatting.GRAY), Text.empty());
        contentAdder.add(contentTextField);
        contentGrid.refreshPositions();
        mainAdder.add(contentGrid);

        var actionGrid = new GridWidget();
        actionGrid.getMainPositioner().alignLeft().margin(2);
        var actionAdder = actionGrid.createAdder(2);
        actionAdder.add(ButtonWidget.builder(Text.literal("Publish"), (a) -> this.publish())
                .dimensions(width / 2 - 205, height - 30, 200, 20)
                .build());
        actionAdder.add(ButtonWidget.builder(Text.literal("Cancel"), (a) -> this.close())
                .dimensions(width / 2 + 5, height - 30, 200, 20)
                .build());
        actionGrid.refreshPositions();
        mainAdder.add(actionGrid);

        mainGrid.refreshPositions();
        mainGrid.forEachChild(this::addDrawableChild);
        setFocused(true);
    }

    private void publish() {
        if(!titleTextField.getText().isEmpty() && !contentTextField.getText().isEmpty()) {
            var comp = new NbtCompound();
            comp.putString("title", titleTextField.getText());
            comp.putString("content", contentTextField.getText());
            ClientPlayNetworking.send(AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_REQUEST, PacketByteBufs.create().writeNbt(comp));
            this.close();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);
    }
}
