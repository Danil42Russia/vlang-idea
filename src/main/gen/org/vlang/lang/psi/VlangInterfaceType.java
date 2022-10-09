// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangInterfaceType extends VlangType, VlangFieldListOwner {

  @NotNull
  List<VlangMembersGroup> getMembersGroupList();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getInterface();

  @NotNull
  List<VlangFieldDefinition> getFieldList();

  @NotNull
  List<VlangInterfaceMethodDefinition> getMethodList();

}
