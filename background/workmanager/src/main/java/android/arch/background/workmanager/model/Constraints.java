/*
 * Copyright (C) 2017 The Android Open Source Project
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

package android.arch.background.workmanager.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

/**
 * The constraints that can be applied to one {@link WorkSpec}.
 */

public class Constraints {
    @Retention(SOURCE)
    @IntDef({NETWORK_TYPE_CONNECTED, NETWORK_TYPE_METERED, NETWORK_TYPE_NONE,
            NETWORK_TYPE_NOT_ROAMING, NETWORK_TYPE_UNMETERED})
    public @interface NetworkType {
    }

    // TODO(xbhatnag): Merge with JobScheduler values.
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_CONNECTED = 1;
    public static final int NETWORK_TYPE_UNMETERED = 2;
    public static final int NETWORK_TYPE_METERED = 3;
    public static final int NETWORK_TYPE_NOT_ROAMING = 4;

    @NetworkType
    int mRequiredNetworkType;
    boolean mRequiresCharging;
    boolean mRequiresDeviceIdle;
    boolean mRequiresBatteryNotLow;
    boolean mRequiresStorageNotLow;
    long mInitialDelay;

    public Constraints() { // stub required for room
    }

    private Constraints(Builder builder) {
        mRequiresCharging = builder.mRequiresCharging;
        mRequiresDeviceIdle = builder.mRequiresDeviceIdle;
        mRequiredNetworkType = builder.mRequiredNetworkType;
        mRequiresBatteryNotLow = builder.mRequiresBatteryNotLow;
        mRequiresStorageNotLow = builder.mRequiresStorageNotLow;
        mInitialDelay = builder.mInitialDelay;
    }

    public int getRequiredNetworkType() {
        return mRequiredNetworkType;
    }

    public void setRequiredNetworkType(int requiredNetworkType) {
        mRequiredNetworkType = requiredNetworkType;
    }

    /**
     * @return If the constraints require charging.
     */
    public boolean requiresCharging() {
        return mRequiresCharging;
    }

    public void setRequiresCharging(boolean requiresCharging) {
        mRequiresCharging = requiresCharging;
    }

    /**
     * @return If the constraints require device idle.
     */
    public boolean requiresDeviceIdle() {
        return mRequiresDeviceIdle;
    }

    public void setRequiresDeviceIdle(boolean requiresDeviceIdle) {
        mRequiresDeviceIdle = requiresDeviceIdle;
    }

    /**
     * @return If the constraints require battery not low status.
     */
    public boolean requiresBatteryNotLow() {
        return mRequiresBatteryNotLow;
    }

    public void setRequiresBatteryNotLow(boolean requiresBatteryNotLow) {
        mRequiresBatteryNotLow = requiresBatteryNotLow;
    }

    /**
     * @return If the constraints require storage not low status.
     */
    public boolean requiresStorageNotLow() {
        return mRequiresStorageNotLow;
    }

    public void setRequiresStorageNotLow(boolean requiresStorageNotLow) {
        mRequiresStorageNotLow = requiresStorageNotLow;
    }

    public long getInitialDelay() {
        return mInitialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        mInitialDelay = initialDelay;
    }

    /**
     * Builder for {@link Constraints} class.
     */
    public static class Builder {
        private boolean mRequiresCharging = false;
        private boolean mRequiresDeviceIdle = false;
        private int mRequiredNetworkType = NETWORK_TYPE_NONE;
        private boolean mRequiresBatteryNotLow = false;
        private boolean mRequiresStorageNotLow = false;
        private long mInitialDelay = 0L;

        /**
         * Specify whether device should be plugged in for {@link WorkSpec} to run.
         * Default is false.
         *
         * @param requiresCharging true if device must be plugged in, false otherwise
         * @return current builder
         */
        public Builder setRequiresCharging(boolean requiresCharging) {
            this.mRequiresCharging = requiresCharging;
            return this;
        }

        /**
         * Specify whether device should be idle for {@link WorkSpec} to run. Default is false.
         *
         * @param requiresDeviceIdle true if device must be idle, false otherwise
         * @return current builder
         */
        public Builder setRequiresDeviceIdle(boolean requiresDeviceIdle) {
            this.mRequiresDeviceIdle = requiresDeviceIdle;
            return this;
        }

        /**
         * Specify whether device should have a particular {@link NetworkType} for {@link WorkSpec}
         * to run. Default is {@value #NETWORK_TYPE_NONE}
         *
         * @param networkType type of network required
         * @return current builder
         */
        public Builder setRequiredNetworkType(@NetworkType int networkType) {
            this.mRequiredNetworkType = networkType;
            return this;
        }

        /**
         * Specify whether device battery should not be below critical threshold for
         * {@link WorkSpec} to run. Default is false.
         *
         * @param requiresBatteryNotLow true if battery should not be below critical threshold,
         *                              false otherwise
         * @return current builder
         */
        public Builder setRequiresBatteryNotLow(boolean requiresBatteryNotLow) {
            this.mRequiresBatteryNotLow = requiresBatteryNotLow;
            return this;
        }

        /**
         * Specify whether device available storage should not be below critical threshold for
         * {@link WorkSpec} to run. Default is false.
         *
         * @param requiresStorageNotLow true if available storage should not be below critical
         *                              threshold, false otherwise
         * @return current builder
         */
        public Builder setRequiresStorageNotLow(boolean requiresStorageNotLow) {
            this.mRequiresStorageNotLow = requiresStorageNotLow;
            return this;
        }

        /**
         * Specify whether {@link WorkSpec} should run with an initial delay. Default is 0ms.
         *
         * @param duration initial delay before running WorkSpec (in milliseconds)
         * @return current builder
         */
        public Builder setInitialDelay(long duration) {
            // TODO(xbhatnag) : Does this affect rescheduled jobs?
            this.mInitialDelay = duration;
            return this;
        }

        /**
         * Generates the {@link Constraints} from this Builder.
         *
         * @return new {@link Constraints} which can be attached to a {@link WorkSpec}
         */
        public Constraints build() {
            return new Constraints(this);
        }
    }
}
