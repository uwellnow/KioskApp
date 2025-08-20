import com.app.stronglife.R

enum class NutritionType(
    val displayName: String,
    val icon: Int,
    val nutrients: Map<String, String> // 영양소 이름 -> 설명
) {
    ENERGY_FOCUS(
        displayName = "에너지/포커스",
        icon = R.drawable.energy,
        nutrients = mapOf(
            "카페인" to "에너지 · 집중 모드",
            "천연카페인" to "에너지 · 집중 모드",
            "테아닌" to "카페인 하모니 · 포커스",
            "티로신" to "집중모드 · 드라이브",
            "비타민B군" to "에너지 · 컨디션 케어"
        )
    ),
    RECOVERY(
        displayName = "리커버리",
        icon = R.drawable.recovery,
        nutrients = mapOf(
            "BCAA" to "아미노산 · 리커버",
            "L-류신" to "아미노산 · 활동 후 리커버",
            "L-이소류신" to "아미노산 · 에너지 밸런스",
            "L-발린" to "아미노산 · 컨디션 케어",
            "EAA" to "밸런스드 아미노산 · 리커버",
            "L-글루타민" to "활동 후 케어 / 아미노산 보충",
            "MPI+WPI" to "편리한 소화 · 단백질 보충",
            "락토프리\n유청 단백질" to "가벼운 소화 / 단백질 보충",
            "락토페린" to "컨디션 / 케어",
            "면역글로불린" to "밸런스 / 컨디션",
        )
    ),
    PUMP_MOTION(
        displayName = "펌프&모션",
        icon = R.drawable.pump,
        nutrients = mapOf(
            "L-아르기닌" to "펌프모드 · 퍼포먼스",
            "수박과피추출물" to "펌프모드 · 지속력",
            "L-시트룰린" to "펌프모드 · 지속력",
            "말릭애씨드" to "운동 후 컨디션 · 지속력",
            "히말라야 핑크솔트" to "펌프모드 · 수분",
            "무수 베타인" to "펌프모드 · 지속력"
        )
    ),
    REFRESH(
        displayName = "리프레시",
        icon = R.drawable.refresh,
        nutrients = mapOf(
            "타우린" to "컨디션 케어 · 수축감",
            "구연산나트륨" to "컨디션 케어 · 리프레시",
            "비타민C" to "활동 전후 리프레시",
            "이놀린" to "가벼운 소화 · 흡수",
            "미네랄" to "활력 / 밸런스"
        )
    )
}

data class NutrientMeta(
    val type: NutritionType,
    val icon: Int,
    val description: String
)

fun getNutrientMeta(name: String): NutrientMeta? {
    val type = NutritionType.values().firstOrNull { it.nutrients.containsKey(name) }
    return type?.let {
        NutrientMeta(
            type = it,
            icon = it.icon,
            description = it.nutrients[name] ?: ""
        )
    }
}

