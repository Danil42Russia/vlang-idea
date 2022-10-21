package org.vlang.sdk

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.ProjectJdkForModuleStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import org.vlang.ide.ui.VIcons

class VlangModuleType : ModuleType<VlangModuleBuilder>("V_MODULE") {
    companion object {
        fun getInstance() = ModuleTypeManager.getInstance().findByID("V_MODULE") as VlangModuleType
    }
    
    override fun createModuleBuilder() = VlangModuleBuilder()

    override fun getName() = "V Module"

    override fun getDescription() = "V modules are used for developing <b>V</b> applications."

    override fun getNodeIcon(isOpened: Boolean) = VIcons.Vlang

    override fun createWizardSteps(
        wizardContext: WizardContext,
        moduleBuilder: VlangModuleBuilder,
        modulesProvider: ModulesProvider,
    ): Array<ModuleWizardStep> {
        return arrayOf(object : ProjectJdkForModuleStep(wizardContext, VlangSdkType.getInstance()) {
            override fun updateDataModel() {
                super.updateDataModel()
                moduleBuilder.moduleJdk = jdk
            }
        })
    }
}
