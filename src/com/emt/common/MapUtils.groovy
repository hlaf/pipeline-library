package com.emt.common

class MapUtils implements Serializable {

    @CoverageIgnoreGenerated
    static boolean isMap(object){
        return object in Map
    }

    @CoverageIgnoreGenerated
    static Map merge(Map base, Map overlay) {

        Map result = [:]

        base = base ?: [:]

        result.putAll(base)

        for (e in overlay) {
            result[e.key] = isMap(e.value) ? merge(base[e.key], e.value) : e.value
        }
        return result
    }
}
