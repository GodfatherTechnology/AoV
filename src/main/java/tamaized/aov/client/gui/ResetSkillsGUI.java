package tamaized.aov.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import tamaized.aov.AoV;
import tamaized.aov.common.capabilities.CapabilityList;
import tamaized.aov.common.capabilities.aov.IAoVCapability;
import tamaized.aov.common.gui.GuiHandler;
import tamaized.aov.network.server.ServerPacketHandlerSpellSkill;
import tamaized.aov.proxy.ClientProxy;

import java.io.IOException;

public class ResetSkillsGUI extends GuiScreenClose {

	private static final int BUTTON_CLOSE = 0;
	private static final int BUTTON_BACK = 1;
	private static final int BUTTON_RESET_MINOR = 2;
	private static final int BUTTON_RESET_FULL = 3;

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(BUTTON_CLOSE, 10, height - 25, 80, 20, I18n.format("aov.gui.button.close")));
		buttonList.add(new GuiButton(BUTTON_BACK, 110, height - 25, 80, 20, I18n.format("aov.gui.button.back")));
		buttonList.add(new GuiButton(BUTTON_RESET_FULL, width - 190, height - 25, 80, 20, I18n.format("aov.gui.button.fullreset")));
		buttonList.add(new GuiButton(BUTTON_RESET_MINOR, width - 90, height - 25, 80, 20, I18n.format("aov.gui.button.minorreset")));

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (mc == null || mc.player == null || !mc.player.hasCapability(CapabilityList.AOV, null))
				return;
			IAoVCapability cap = mc.player.getCapability(CapabilityList.AOV, null);
			if (cap == null)
				return;
			switch (button.id) {
				case BUTTON_CLOSE:
					mc.displayGuiScreen(null);
					break;
				case BUTTON_BACK:
					GuiHandler.openGUI(GuiHandler.GUI_SKILLS, mc.player, mc.world);
					break;
				case BUTTON_RESET_MINOR:
					if (cap.getObtainedSkills().size() > 1)
						AoV.network.sendToServer(new ServerPacketHandlerSpellSkill.Packet(ServerPacketHandlerSpellSkill.Packet.PacketType.RESETSKILLS_MINOR, 0, null));
					break;
				case BUTTON_RESET_FULL:
					AoV.network.sendToServer(new ServerPacketHandlerSpellSkill.Packet(ServerPacketHandlerSpellSkill.Packet.PacketType.RESETSKILLS_FULL, 0, null));
					ClientProxy.barToggle = false;
					break;
				default:
					break;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {

	}

	@Override
	public void updateScreen() {
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, I18n.format("aov.gui.title.reset"), width / 2, 15, 16777215);
		fontRenderer.drawSplitString(I18n.format("aov.gui.reset.full"), 40, 40, width - 80, 0x00FFFF);
		fontRenderer.drawSplitString(I18n.format("aov.gui.reset.minor"), 40, 125, width - 80, 0xFFFF00);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
