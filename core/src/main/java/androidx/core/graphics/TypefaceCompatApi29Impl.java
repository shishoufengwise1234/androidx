/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.core.graphics;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;

import java.io.IOException;
import java.io.InputStream;

/** @hide */
@RestrictTo(LIBRARY_GROUP)
@RequiresApi(29)
public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    @Override
    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fonts,
            int style) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    // Caller must close the stream.
    @Override
    protected Typeface createFromInputStream(Context context, InputStream is) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    @Nullable
    @Override
    public Typeface createFromFontInfo(Context context,
            @Nullable CancellationSignal cancellationSignal,
            @NonNull FontsContractCompat.FontInfo[] fonts, int style) {
        FontFamily.Builder familyBuilder = null;
        final ContentResolver resolver = context.getContentResolver();
        for (FontsContractCompat.FontInfo font : fonts) {
            try (ParcelFileDescriptor pfd = resolver.openFileDescriptor(font.getUri(), "r",
                    cancellationSignal)) {
                if (pfd == null) {
                    continue;  // keep adding succeeded fonts.
                }
                final Font platformFont = new Font.Builder(pfd.getFileDescriptor())
                        .setWeight(font.getWeight())
                        .setSlant(font.isItalic() ? FontStyle.FONT_SLANT_ITALIC
                                : FontStyle.FONT_SLANT_UPRIGHT)
                        .setTtcIndex(font.getTtcIndex())
                        .build();  // TODO: font variation settings?
                if (familyBuilder == null) {
                    familyBuilder = new FontFamily.Builder(platformFont);
                } else {
                    familyBuilder.addFont(platformFont);
                }
            } catch (IOException e) {
                // keep adding succeeded fonts.
            }
        }
        if (familyBuilder == null) {
            return null;  // No font is added. Give up.
        }
        final FontStyle defaultStyle = new FontStyle(
                (style & Typeface.BOLD) != 0 ? FontStyle.FONT_WEIGHT_BOLD
                        : FontStyle.FONT_WEIGHT_NORMAL,
                (style & Typeface.ITALIC) != 0 ? FontStyle.FONT_SLANT_ITALIC
                        : FontStyle.FONT_SLANT_UPRIGHT
        );
        return new Typeface.CustomFallbackBuilder(familyBuilder.build())
                .setStyle(defaultStyle)
                .build();
    }

    @Nullable
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context context,
            FontResourcesParserCompat.FontFamilyFilesResourceEntry familyEntry, Resources resources,
            int style) {
        FontFamily.Builder familyBuilder = null;
        for (FontResourcesParserCompat.FontFileResourceEntry entry : familyEntry.getEntries()) {
            try {
                final Font platformFont = new Font.Builder(resources, entry.getResourceId())
                        .setWeight(entry.getWeight())
                        .setSlant(entry.isItalic() ? FontStyle.FONT_SLANT_ITALIC
                                : FontStyle.FONT_SLANT_UPRIGHT)
                        .setTtcIndex(entry.getTtcIndex())
                        .setFontVariationSettings(entry.getVariationSettings())
                        .build();
                if (familyBuilder == null) {
                    familyBuilder = new FontFamily.Builder(platformFont);
                } else {
                    familyBuilder.addFont(platformFont);
                }
            } catch (IOException e) {
                // keep adding succeeded fonts
            }
        }
        if (familyBuilder == null) {
            return null;  // No font is added. Give up
        }
        final FontStyle defaultStyle = new FontStyle(
                (style & Typeface.BOLD) != 0 ? FontStyle.FONT_WEIGHT_BOLD
                        : FontStyle.FONT_WEIGHT_NORMAL,
                (style & Typeface.ITALIC) != 0 ? FontStyle.FONT_SLANT_ITALIC
                        : FontStyle.FONT_SLANT_UPRIGHT
        );
        return new Typeface.CustomFallbackBuilder(familyBuilder.build())
                .setStyle(defaultStyle)
                .build();
    }

    /**
     * Used by Resources to load a font resource of type font file.
     */
    @Nullable
    @Override
    public Typeface createFromResourcesFontFile(
            Context context, Resources resources, int id, String path, int style) {
        FontFamily family = null;
        try {
            family = new FontFamily.Builder(new Font.Builder(resources, id).build()).build();
        } catch (IOException e) {
            return null;
        }
        final FontStyle defaultStyle = new FontStyle(
                (style & Typeface.BOLD) != 0 ? FontStyle.FONT_WEIGHT_BOLD
                        : FontStyle.FONT_WEIGHT_NORMAL,
                (style & Typeface.ITALIC) != 0 ? FontStyle.FONT_SLANT_ITALIC
                        : FontStyle.FONT_SLANT_UPRIGHT
        );
        return new Typeface.CustomFallbackBuilder(family).setStyle(defaultStyle).build();
    }

}
