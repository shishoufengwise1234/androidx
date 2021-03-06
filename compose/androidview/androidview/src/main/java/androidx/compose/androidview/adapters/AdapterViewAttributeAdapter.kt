/*
 * Copyright 2019 The Android Open Source Project
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

@file:Suppress("unused")

package androidx.compose.androidview.adapters

import android.widget.AdapterView
import androidx.compose.androidview.annotations.ConflictsWith
import androidx.compose.androidview.annotations.RequiresOneOf

private val key = tagKey("AdapterViewInputController")

private val AdapterView<*>.controller: AdapterViewInputController
    get() {
        var listener = getTag(key) as? AdapterViewInputController
        if (listener == null) {
            listener = AdapterViewInputController(this)
            setTag(key, listener)
            onItemSelectedListener = listener
        }
        return listener
    }

@RequiresOneOf("controlledSelectedIndex")
@ConflictsWith("onItemSelectedListener")
fun AdapterView<*>.setOnSelectedIndexChange(listener: (Int) -> Unit) {
    controller.onSelectedIndexChange = listener
}

@RequiresOneOf("onSelectedIndexChange")
@ConflictsWith("selection")
fun AdapterView<*>.setControlledSelectedIndex(selectedIndex: Int) {
    controller.setValueIfNeeded(selectedIndex)
}