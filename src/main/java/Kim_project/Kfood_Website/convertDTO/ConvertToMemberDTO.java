package Kim_project.Kfood_Website.convertDTO;

import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Recipe;
import Kim_project.Kfood_Website.dto.MemberDTO;
import Kim_project.Kfood_Website.dto.RecipeDTO;

import java.util.ArrayList;
import java.util.List;

public class ConvertToMemberDTO {
    public List<MemberDTO> convertToMemberDTO(List<Member> members) {

        List<MemberDTO> memberDTOS = new ArrayList<>();

        for (Member member : members) {
            MemberDTO memberDTO = new MemberDTO();

            memberDTO.setUserId(member.getMemberId());
            memberDTO.setAuthentication(member.getMemberAuthority());
            memberDTO.setRealName(member.getMemberName());
            memberDTO.setPhoneNumber(member.getMemberPhoneNumber());
            memberDTO.setPassword(member.getMemberPassword());
            memberDTO.setEmailAddress(member.getMemberEmail());

            memberDTOS.add(memberDTO);
        }
        return memberDTOS;
    }
}
