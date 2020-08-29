package kr.hs.entrydsm.husky.domain.pdf.util;

import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;

public class FontLoader {

    public void addPhysicalFonts() {
        PhysicalFonts.addPhysicalFonts("KoPubWorldDotum_Pro Light", this.getClass().getResource("/fonts/KoPubWorld_Dotum_Light.ttf"));
        PhysicalFonts.addPhysicalFonts("KoPubWorldDotum_Pro Medium", this.getClass().getResource("/fonts/KoPubWorld_Dotum_Medium.ttf"));
        PhysicalFonts.addPhysicalFonts("KoPubWorldDotum_Pro Bold", this.getClass().getResource("/fonts/KoPubWorld_Dotum_Bold.ttf"));
        PhysicalFonts.addPhysicalFonts("KoPubWorldDotum_Pro", this.getClass().getResource("/fonts/KoPubWorld_Dotum_Bold.ttf"));
        PhysicalFonts.addPhysicalFonts("DejaVu Sans Book", this.getClass().getResource("/fonts/DejaVuSans.ttf"));
    }

    public Mapper createFontMapperInstance() {
        Mapper fontMapper = new IdentityPlusMapper();
        fontMapper.put("KoPubWorldDotum_Pro Medium", PhysicalFonts.get("KoPubWorldDotum Medium"));
        fontMapper.put("KoPubWorldDotum_Pro Light", PhysicalFonts.get("KoPubWorldDotum Light"));
        fontMapper.put("KoPubWorldDotum_Pro Bold", PhysicalFonts.get("KoPubWorldDotum Bold"));
        fontMapper.put("KoPubWorldDotum_Pro", PhysicalFonts.get("KoPubWorldDotum Bold"));
        fontMapper.put("DejaVu Sans Book", PhysicalFonts.get("DejaVu Sans"));
        fontMapper.put("Helvetica", PhysicalFonts.get("DejaVu Sans"));
        fontMapper.put("Helvetica-Bold", PhysicalFonts.get("DejaVu Sans"));
        fontMapper.put("Calibri", PhysicalFonts.get("DejaVu Sans"));

        return fontMapper;
    }

}
