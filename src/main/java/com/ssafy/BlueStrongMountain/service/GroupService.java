package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.GroupCreateRequest;
import com.ssafy.BlueStrongMountain.dto.GroupDetailDto;
import com.ssafy.BlueStrongMountain.dto.GroupSummaryDto;
import com.ssafy.BlueStrongMountain.dto.GroupUpdateRequest;
import java.util.List;

public interface GroupService {
    /**
 * 새 그룹을 생성한다.
 *
 * @param ownerId 그룹 소유자(생성자)의 사용자 식별자
 * @param request 그룹 생성에 필요한 정보(예: 이름, 설명 등)
 * @return 생성된 그룹의 식별자(ID)
 */
Long createGroup(Long ownerId, GroupCreateRequest request);
    /**
 * 요청자 관점에서 특정 그룹의 상세 정보를 조회한다.
 *
 * @param requesterId 조회를 요청한 사용자의 식별자
 * @param groupId     조회 대상 그룹의 식별자
 * @return 요청자에게 제공되는 그룹의 상세 정보가 담긴 GroupDetailDto
 */
GroupDetailDto getGroupDetail(Long requesterId, Long groupId);
    /**
 * 요청자가 소유하거나 접근 가능한 그룹들의 요약 목록을 조회한다.
 *
 * @param requesterId 조회를 요청한 사용자의 회원 식별자
 * @return 요청자가 소유하거나 접근 가능한 그룹의 요약 정보 목록
 */
List<GroupSummaryDto> findMyGroups(Long requesterId);
    /**
 * 요청자가 접근하거나 소유한 그룹을 이름으로 검색한다.
 *
 * @param requesterId 검색을 요청한 사용자의 식별자
 * @param name 검색할 그룹 이름 또는 이름의 일부
 * @return 요청자가 접근하거나 소유한 그룹 중 지정한 이름과 일치하거나 이름을 포함하는 그룹들의 요약 DTO 목록
 */
List<GroupSummaryDto> searchMyGroups(Long requesterId, String name);
    /**
 * 지정된 그룹의 정보를 요청자 권한으로 갱신한다.
 *
 * @param requesterId 갱신을 요청한 사용자의 식별자(권한 확인에 사용)
 * @param groupId     갱신할 그룹의 식별자
 * @param request     갱신할 필드를 담은 요청 객체
 */
void updateGroup(Long requesterId, Long groupId, GroupUpdateRequest request);
    void changeOwner(Long requesterId, Long groupId, Long newOwnerId);
}