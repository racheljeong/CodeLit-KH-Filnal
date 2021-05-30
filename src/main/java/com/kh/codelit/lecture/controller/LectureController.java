package com.kh.codelit.lecture.controller;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.kh.codelit.attachment.model.exception.AttachmentException;
import com.kh.codelit.attachment.model.vo.Attachment;
import com.kh.codelit.common.HelloSpringUtils;
import com.kh.codelit.lecture.model.service.LectureService;
import com.kh.codelit.lecture.model.vo.Lecture;
import com.kh.codelit.lecture.model.vo.LectureChapter;
import com.kh.codelit.lecture.model.vo.LecturePart;
import com.kh.codelit.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
@RequestMapping("/lecture")
public class LectureController {

	@Autowired
	private LectureService lectureService;

	@GetMapping("/lectureEnroll.do")
	public void lectureEnroll() {
	}

	@PostMapping("/lectureEnroll.do")
	public String lectureEnroll(
			@ModelAttribute Lecture lecture,
			@RequestParam(required = false) MultipartFile lectureThumbnail,
			@RequestParam(value = "lectureHandout", required = false) MultipartFile[] lectureHandouts,
			@RequestParam String curriculum,
			@RequestParam(value = "chapterVideo", required = false) MultipartFile[] chapterVideos,
			@RequestParam(value = "videoChapNoArr") String videoChapNoArrJsonStr,
			HttpServletRequest request,
			Authentication authentication,
			RedirectAttributes redirectAttr) {

		try {
			log.debug("lecture(필드값 Set 전) = {}", lecture);

			Gson gson = new Gson();

			LecturePart[] lecturePartArr = gson.fromJson(curriculum, LecturePart[].class);
			log.debug("lecturePartArr = {}", lecturePartArr);

//			log.info("chapterVideos = {}", chapterVideos);

			int[] videoChapNoArr = gson.fromJson(videoChapNoArrJsonStr, int[].class);
			List<Integer> videoChapNoList = Arrays.stream(videoChapNoArr).boxed().collect(Collectors.toList());
			log.debug("videoChapNoArr = {}", videoChapNoArr);
			log.debug("videoChapNoList = {}", videoChapNoList);

			//0.파일 저장 및 Attachment객체 생성/썸네일 Filename Set
			String thumbnailsSaveDirectory =
					request.getServletContext().getRealPath(Attachment.PATH_LECTURE_THUMBNAIL);
			String handoutsSaveDirectory =
					request.getServletContext().getRealPath(Attachment.PATH_LECTURE_HANDOUT);
			String videosSaveDirectory =
					request.getServletContext().getRealPath(Attachment.PATH_LECTURE_VIDEO);

			File dirThumb = new File(thumbnailsSaveDirectory);
			if(!dirThumb.exists()) {
				dirThumb.mkdirs(); // 복수개 폴더 생성 가능 (경로상에 없는거 다 만들어줌)
			}

			File dirHandout = new File(handoutsSaveDirectory);
			if(!dirHandout.exists()) {
				dirHandout.mkdirs(); // 복수개 폴더 생성 가능 (경로상에 없는거 다 만들어줌)
			}

			File dirVideo = new File(videosSaveDirectory);
			if(!dirVideo.exists()) {
				dirVideo.mkdirs();
			}

			if(!lectureThumbnail.isEmpty() || !(lectureThumbnail.getSize() == 0)) {
				log.debug("lectureThumbnail = {}", lectureThumbnail);
				log.debug("lectureThumbnail.name = {}", lectureThumbnail.getOriginalFilename());
				log.debug("lectureThumbnail.size = {}", lectureThumbnail.getSize());

				//저장할 파일명 생성
				File renamedFile = HelloSpringUtils.getRenamedFile(thumbnailsSaveDirectory, lectureThumbnail.getOriginalFilename());
				//파일 저장
				//lectureThumbnail.transferTo(renamedFile);

				lecture.setLectureThumbOrigin(lectureThumbnail.getOriginalFilename());
				lecture.setLectureThumbRenamed(renamedFile.getName());
			}

			List<Attachment> attachList = new ArrayList<>();

			for(MultipartFile lectureHandout : lectureHandouts) {
				if(lectureHandout.isEmpty() || lectureHandout.getSize() == 0)
					continue;

				log.debug("lectureHandout = {}", lectureHandouts);
				log.debug("lectureHandout.name = {}", lectureHandout.getOriginalFilename());
				log.debug("lectureHandout.size = {}", lectureHandout.getSize());

				//저장할 파일명 생성
				File renamedFile = HelloSpringUtils.getRenamedFile(handoutsSaveDirectory, lectureHandout.getOriginalFilename());
				//파일 저장
				//lectureHandout.transferTo(renamedFile);
				//Attachment객체 생성
				Attachment attach = new Attachment();
				attach.setOriginalFilename(lectureHandout.getOriginalFilename());
				attach.setRenamedFilename(renamedFile.getName());
				attach.setRefContentsGroupCode(Attachment.CODE_LECTURE_HANDOUT);

				attachList.add(attach);
			}

			int vIdx = 0;
			int prevChapterArrlength = 0;
			for(int i = 0; i < lecturePartArr.length; i++) {
				log.debug("lecturePart[{}] = {}", i, lecturePartArr[i]);

				LectureChapter[] lectureChapterArr = lecturePartArr[i].getChapterArr();
				int ChapterArrlength = lectureChapterArr.length;

				if(i > 0)
					prevChapterArrlength += lecturePartArr[i-1].getChapterArr().length;

				for(int j = 0; j < ChapterArrlength; j++) {
					if(videoChapNoList.contains(prevChapterArrlength + j)) {
						while(chapterVideos[vIdx].isEmpty() || chapterVideos[vIdx].getSize() == 0) {
							vIdx++;
						}

						//log.debug("vIdx = {}", vIdx);

						MultipartFile currentChapterVideo = chapterVideos[vIdx++];
						log.debug("currentChapterVideo = {}", currentChapterVideo);
						log.debug("currentChapterVideo.name = {}", currentChapterVideo.getOriginalFilename());
						log.debug("currentChapterVideo.size = {}", currentChapterVideo.getSize());

						File renamedFile = HelloSpringUtils.getRenamedFile(videosSaveDirectory, currentChapterVideo.getOriginalFilename());
						//currentChapterVideo.transferTo(renamedFile);

						lectureChapterArr[j].setLecChapterVideo(currentChapterVideo.getOriginalFilename());
						lectureChapterArr[j].setLecChapterReVideo(renamedFile.getName());
					}
					log.debug("lectureChapter[{}] = {}", j, lectureChapterArr[j]);
				}
			}

			//1. 업무로직
			lecture.setAttachList(attachList);
			lecture.setRefMemberId(((Member)authentication.getPrincipal()).getMemberId());

			Map<String, Object> param = new HashMap<>();
			param.put("lecture", lecture);
			param.put("lecturePartArr", lecturePartArr);

			log.debug("lecture(필드값 Set 후) = {}", lecture);

			//int result = lectureService.insertLecture(param);

			//2. 사용자 피드백
			//String msg = result > 0 ? "게시글 등록 성공!" : "게시글 등록 실패!";
			//redirectAttr.addFlashAttribute("msg", msg);

//		} catch (IOException | IllegalStateException e) {
		} catch (IllegalStateException e) {
			log.error("첨부파일 등록 오류!", e);
			throw new AttachmentException("첨부파일 등록 오류!"); //Checked Exception은 throw로 바로 던질수 없으니, 커스팀 예외 객체를 만들어 던져준다.
		} catch (Exception e) {
			log.error("강의 등록 오류!", e);
			throw e;
		}

		return "redirect:/lecture/lectureEnroll.do";
	}

	@GetMapping(value = {"/lectureList.do/{catNo}", "/lectureList.do"})
	public String lectureList(
			@PathVariable(required = false) Integer catNo,
			@RequestParam(defaultValue = "1") int cPage,
			HttpServletRequest request,
			Model model,
			Principal principal) {
		//1. 사용자 입력값
		if(catNo == null)
			catNo = 0;
		int numPerPage = 12;
		String memberId = principal != null ? principal.getName() : null;
		log.debug("catNo = {}", catNo);
		log.debug("cPage = {}", cPage);
		log.debug("memberId = {}", memberId);
		Map<String, Object> param = new HashMap<>();
		param.put("numPerPage", numPerPage);
		param.put("catNo", catNo);
		param.put("cPage", cPage);
		param.put("memberId", memberId);

		//2. 업무로직
		//a. contents영역
		List<Lecture> list = lectureService.selectLectureList(param);
		List<Object> orderedlectureNoList = lectureService.selectOrderedLectureList(memberId);
		log.debug("list = {}", list);
		log.debug("orderedlectureNoList = {}", orderedlectureNoList);

		//b. pageBar영역
		int totalContents = lectureService.getTotalContents(catNo);
		String url = HelloSpringUtils.convertToParamUrl(request);
		log.debug("totalContents = {}", totalContents);
		log.debug("url = {}", url);
		String pageBar = HelloSpringUtils.getPageBar(totalContents, cPage, numPerPage, url);

		//3.jsp 위임처리
		model.addAttribute("list", list);
		model.addAttribute("orderedlectureNoList", orderedlectureNoList);
		model.addAttribute("pageBar", pageBar);

		return "lecture/lectureList";
	}

	@GetMapping("/lectureDetail.do")
	public ModelAndView lectureDetail(HttpServletRequest request,
									  @RequestParam int no,
									  ModelAndView mav,
									  Authentication authentication) {
		//1. 업무로직


		// 로그인 정보
		Member loginMember = (Member)authentication.getPrincipal();
		//log.debug("loginMember = {}", loginMember);
		loginMember.getMemberId();
		log.debug("loginMember id = {}", loginMember);


		Lecture lecture = lectureService.selectOneLecture(no);
		lecture.setLectureCommentList(lectureService.selectLectureCmtList(no));
		int numPerCmtPage = 5;
		int totalCmtPage = (int)Math.ceil((double)lecture.getLectureCommentList().size() / numPerCmtPage);
		log.debug("lecture = {}", lecture);
		log.debug("totalCmtPage = {}", totalCmtPage);


		//map객체에 담아보기
		Map<String,Object> param = new HashMap<>();
		param.put("ref_member_id", loginMember.getMemberId());
		param.put("no", no);
		log.debug("param = {}", param);

		//강의id 담아 클릭수
		int result = lectureService.clickCount(param);
		log.debug("clickCountresult = {}", result);

		//2. jsp 위임
		mav.addObject("lecture", lecture);
		mav.addObject("numPerCmtPage", numPerCmtPage);
		mav.addObject("totalCmtPage", totalCmtPage);
		mav.setViewName("lecture/lectureDetail");

		return mav;
	}


	@GetMapping("/mainSearchResult.do")
	public ModelAndView mainSearchResult(ModelAndView mav,
										@RequestParam(required = false) String keyword
										)
	{

		//log.debug("메인 검색 컨트롤러 연결 완@searchKeyword = {}", keyword);

	try {
		Map<String, Object> param = new HashMap<>();
		param.put("searchKeyword", keyword);
		log.debug("param = {}", param);

		List<Map<String, Object>> list = lectureService.mainSearchResult(param);
		log.debug("mainSearchResult@list = {}", list);

		mav.addObject("list",list);
		mav.setViewName("/lecture/mainSearchResult");
	}catch(Exception e) {
		throw e;
	}

		return mav;
	}



	@GetMapping("/lecture.do")
	public void lecture(
				// 강의번호와 영상챕터번호 받아옴. (영상챕터번호 없다면, 막 상세보기면 -1 배정)
				@RequestParam(defaultValue = "3") int lectureNo,
				@RequestParam(defaultValue="-1") int chapterNo,
				Model model,
				Authentication authentication

			) {

		try {
			// 아이디 추출
			Member loginMember = (Member)authentication.getPrincipal();
			String refMemberId = loginMember.getMemberId();

			// 강의 추출
			Lecture lecture = lectureService.selectOneLecture(lectureNo);

			// 매개변수
			Map<String, Object> param = new HashMap<>();
			param.put("refMemberId", refMemberId);
			param.put("refLectureNo", 3);

			// 챕터번호와 영상시청여부 추출 (chapterNo, yn)
			List<Map<String, Object>> progList = lectureService.selectLectureProgress(param);

			log.debug("{}", lecture);
			log.debug("{}", progList);

			int playPosition = 0;
			// 지정된 챕터번호가 없다면 yn 보고 찾음
			if(chapterNo == -1) {
				// 재생할 영상 번호 기본값 : 첫 번째 영상번호
				playPosition = Integer.parseInt((progList.get(0).get("chapterNo")).toString());

				// 두번째 영상 번호부터 yn여부 판별
				for(int i=1; i<progList.size(); i++) {
					// Y가 아닌 것을 찾으면 그 바로 전 번호 영상을 재생 (전부 Y이면 기본값 적용)
					if(!progList.get(i).get("yn").equals("Y")) {
						playPosition = Integer.parseInt((progList.get(i-1).get("chapterNo")).toString());
						break;
					}
				}
			} else {
				playPosition = chapterNo;
			}

			log.debug("재생할 번호 찾음 = {}", playPosition);
			// 챕터번호에 맞는 영상 이름 가져오기.
			String videoRename = lectureService.selectVideoRename(playPosition);

			// refMemberId, refLectureNo, chapterNo 3개가 담긴 파라미터맵.
			param.put("chapterNo", playPosition);
			// 재생할 동영상의 yn을 Y로 바꿔줌. (param에서 memberId, chapterNo 사용)
			int result = lectureService.updateProgress(param);

			for(Map<String, Object> prog : progList) {
				if(Integer.parseInt(prog.get("chapterNo").toString()) == playPosition) {
					prog.put("yn", "Y");
				}
			}


			model.addAttribute("lecture", lecture);
			model.addAttribute("progList", progList);
			// 저장 챕터번호 및 비디오이름
			model.addAttribute("playPosition", playPosition);
			model.addAttribute("videoRename", videoRename);

		} catch(Exception e) {
			throw e;

		}

	}



	@GetMapping("/myAllLecture.do")
	public ModelAndView myAllLecture(ModelAndView mav,
									 Authentication authentication,
									 HttpServletRequest request,
									 @RequestParam(defaultValue ="1") int cPage) {


		//페이징처리
		int numPerPage = 10;

	try {
		// 로그인 정보
		Member loginTeacher = (Member)authentication.getPrincipal();
		log.debug("loginTeacher = {}", loginTeacher);
		loginTeacher.getMemberId();
		log.debug("loginTeacher.id = {}", loginTeacher.getMemberId());
		//loginTeacher.id = teacher

		Map<String, Object> param = new HashMap<>();
		param.put("cPage", cPage);
		param.put("numPerPage", numPerPage);
		param.put("teacherId", loginTeacher.getMemberId());
		log.debug("강의자의 내 강의 보기 param = {}", param);

		// b. pageBar 영역
		int totalContents = lectureService.getTeacherTotalContents(param);
		log.debug("강의자 목록 totalContents = {}", totalContents);
		List<Map<String,Object>> list = lectureService.myAllLecture(param);
		log.debug("list = {}", list);


		String url = HelloSpringUtils.convertToParamUrl(request);
		log.debug("url = {}", url);
		String pageBar = HelloSpringUtils.getPageBar(totalContents, cPage, numPerPage, url);
		log.debug("pageBar = {}", pageBar);

		mav.addObject("list",list);
		mav.addObject("pageBar",pageBar);
		mav.setViewName("/teacher/myAllLecture");

	}catch(Exception e) {
		throw e;
	}

		return mav;
	}

}