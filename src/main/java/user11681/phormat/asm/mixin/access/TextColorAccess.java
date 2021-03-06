package user11681.phormat.asm.mixin.access;

import java.util.Map;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("ConstantConditions")
@Mixin(TextColor.class)
public interface TextColorAccess {
    @Accessor("FORMATTING_TO_COLOR")
    static Map<Formatting, TextColor> getFormattingColors() {
        throw null;
    }

    @Accessor("FORMATTING_TO_COLOR")
    static void setFormattingColors(Map<Formatting, TextColor> colors) {
        throw null;
    }

    @Invoker("<init>")
    static TextColor instantiate(int color, String name) {
        throw null;
    }
}
