package com.pfchoice.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipFollowup;

@Repository
public interface MembershipFollowupRepository extends JpaRepository<MembershipFollowup, Integer> {

	@Query("select mf from membership_followup mf where mbr_id= :mbrId order by created_date desc")
	public List<MembershipFollowup> findAllMembershipFollowupsByMbrId(@Param("mbrId") Integer mbrId);
}
