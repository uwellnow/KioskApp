import com.app.stronglife.R
import com.app.stronglife.ui.screen.menuScreen.NutrientMeta

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
            "비타민B군" to "에너지 · 컨디션 케어",
            "L-티로신" to "집중모드 · 포커스"
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
            "EAA" to "밸런스 아미노산 · 리커버",
            "L-글루타민" to "활동 후 케어 / 아미노산 보충",
            "MPI+WPI" to "편리한 소화 · 단백질 보충",
            "락토프리\n유청 단백질" to "가벼운 소화 / 단백질 보충",
            "락토페린" to "컨디션 / 케어",
            "면역글로불린" to "밸런스 / 컨디션",
            "탄수화물" to "아미노산 · 회복 모드",
            "단백질" to "아미노산 · 컨디션 케어",
            "L-로이신" to "아미노산 · 리커버",
            "L-라이신염산염" to "아미노산 · 리커버",
            "L-페닐알라닌" to "아미노산 · 리커버",
            "L-트레오닌" to "아미노산 · 리커버",
            "식물성 단백질" to "근육회복 · 단백질 보충",
            "대체당, 화학첨가물" to "-",
            "퀘르세틴" to "일상 컨디션 · 밸런스",
            "과당" to "에너지 보충 · 컨디션 케어",
            "말토덱스트린" to "에너지 보충 · 컨디션 케어",
            "분리유청단백" to "편리한 소화 · 단백질 보충",
            "코코아농축분말" to "풍미원료"
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
            "무수 베타인" to "펌프모드 · 지속력",
            "베타인" to "펌프모드 · 지속력",
            "글리세린" to "펌프모드 · 수분",
            "레드비트파우더" to "펌프모드 · 지속력",
            "포타슘" to "컨디션 케어 · 수분",
            "히말라야핑크솔트" to "펌프모드 · 수분"
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
            "미네랄" to "활력 / 밸런스",
            "지방" to "컨디션 케어 · 면역",
            "케일" to "가벼운 흡수 · 리프레시",
            "블랙커런트" to "가벼운 흡수 · 리프레시",
            "돼지감자" to "가벼운 흡수 · 리프레시",
            "치커리" to "가벼운 흡수 · 리프레시",
            "흰민들레" to "가벼운 흡수 · 리프레시",
            "알파-시클로덱스트린" to "가벼운 흡수 · 리프레시",
            "난소화성말토덱스트린" to "가벼운 흡수 · 리프레시",
            "차전자피" to "가벼운 흡수 · 리프레시",
            "해조칼슘" to "컨디션 케어 · 근육 기능 향상",
            "아쿠아민 마그네슘" to "미네랄 밸런스 · 피로케어",
            "국산귀리" to "가벼운 소화 · 흡수",
            "당" to "가벼운 단맛 · 리프레시"
        )
    )
}

fun getNutrientMeta(name: String): NutrientMeta<NutritionType>? {
    println("getNutrientMeta called with name: '$name'")
    val type = NutritionType.values().firstOrNull { it.nutrients.containsKey(name) }
    println("Found type: ${type?.displayName}")
    return type?.let {
        NutrientMeta(
            type = it,
            icon = it.icon,
            description = it.nutrients[name] ?: "",
            displayName = it.displayName
        )
    }
}

