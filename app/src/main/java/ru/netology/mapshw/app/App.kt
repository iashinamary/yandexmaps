package ru.netology.mapshw.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.netology.mapshw.BuildConfig


class App : Application()  {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}