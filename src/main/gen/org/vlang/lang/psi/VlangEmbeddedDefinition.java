// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangEmbeddedDefinition extends VlangTypeOwner {

  @NotNull
  VlangType getType();

  @NotNull
  VlangTypeEx getType(@Nullable ResolveState context);

}
