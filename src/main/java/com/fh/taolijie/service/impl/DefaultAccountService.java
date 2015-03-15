package com.fh.taolijie.service.impl;

import cn.fh.security.utils.CredentialUtils;
import com.fh.taolijie.controller.dto.EmployerDto;
import com.fh.taolijie.controller.dto.GeneralMemberDto;
import com.fh.taolijie.controller.dto.RoleDto;
import com.fh.taolijie.controller.dto.StudentDto;
import com.fh.taolijie.domain.EducationExperienceEntity;
import com.fh.taolijie.domain.MemberEntity;
import com.fh.taolijie.domain.MemberRoleEntity;
import com.fh.taolijie.domain.RoleEntity;
import com.fh.taolijie.exception.checked.DuplicatedUsernameException;
import com.fh.taolijie.exception.checked.PasswordIncorrectException;
import com.fh.taolijie.exception.checked.UserNotExistsException;
import com.fh.taolijie.service.AccountService;
import com.fh.taolijie.utils.Constants;
import com.fh.taolijie.utils.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wanghongfei on 15-3-5.
 */
@Repository
public class DefaultAccountService implements AccountService {
    private Logger logger = LoggerFactory.getLogger(DefaultAccountService.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public boolean registerStudent(StudentDto stuDto) throws DuplicatedUsernameException {
        if (true == isUserExists(stuDto.getUsername())) {
            throw new DuplicatedUsernameException("用户名[" + stuDto.getUsername() + "]已存在");
        }

        // 创建实体
        MemberEntity mem = new MemberEntity(stuDto.getUsername(), CredentialUtils.sha(stuDto.getPassword()), stuDto.getEmail(),
                stuDto.getName(), stuDto.getStudentId(), stuDto.getGender(), Constants.VerifyStatus.NONE.toString(),
                stuDto.getProfilePhotoPath(), stuDto.getPhone(), stuDto.getQq(), stuDto.getAge(), "", "");
        // 保存实体
        em.persist(mem);

        // 得到角色
        Collection<MemberRoleEntity> memRoleCollection = new ArrayList<>();
        List<Integer> idList = stuDto.getRoleIdList();
        for (Integer id : idList) {
            RoleEntity role = em.getReference(RoleEntity.class, id);
            // 创建关联实体
            MemberRoleEntity mr = new MemberRoleEntity();
            mr.setRole(role);
            mr.setMember(mem);
            em.persist(mr);

            // 将关联添加到member实体中
            memRoleCollection.add(mr);
        }
        mem.setMemberRoleCollection(memRoleCollection);

        return true;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Integer register(GeneralMemberDto dto) throws DuplicatedUsernameException {
        if (true == isUserExists(dto.getUsername())) {
            throw new DuplicatedUsernameException("用户名[" + dto.getUsername() + "]已存在");
        }

        // 创建实体
        MemberEntity mem = new MemberEntity(dto.getUsername(), CredentialUtils.sha(dto.getPassword()), dto.getEmail(),
                dto.getName(), "", dto.getGender(), Constants.VerifyStatus.NONE.toString(),
                dto.getProfilePhotoPath(), dto.getPhone(), dto.getQq(), dto.getAge(), "", "");
        // 保存实体
        em.persist(mem);

        // 得到角色
        Collection<MemberRoleEntity> memRoleCollection = new ArrayList<>();
        List<Integer> idList = dto.getRoleIdList();
        for (Integer id : idList) {
            RoleEntity role = em.getReference(RoleEntity.class, id);
            // 创建关联实体
            MemberRoleEntity mr = new MemberRoleEntity();
            mr.setRole(role);
            mr.setMember(mem);
            em.persist(mr);

            // 将关联添加到member实体中
            memRoleCollection.add(mr);
        }
        mem.setMemberRoleCollection(memRoleCollection);

        em.flush();
        return mem.getId();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public boolean registerEmployer(EmployerDto empDto) throws DuplicatedUsernameException {
        if (true == isUserExists(empDto.getUsername())) {
            throw new DuplicatedUsernameException("用户名[" + empDto.getUsername() + "]已存在");
        }

        // create entity
        MemberEntity mem = new MemberEntity(empDto.getUsername(), CredentialUtils.sha(empDto.getPassword()), empDto.getEmail(),
                empDto.getName(), "", empDto.getGender(), Constants.VerifyStatus.NONE.toString(),
                empDto.getProfilePhotoPath(), empDto.getPhone(), empDto.getQq(), empDto.getAge(), empDto.getCompanyName(), "");

        // 保存用户实体
        em.persist(mem);

        // 得到角色
        Collection<MemberRoleEntity> memRoleCollection = new ArrayList<>();
        List<Integer> idList = empDto.getRoleIdList();
        for (Integer id : idList) {
            RoleEntity role = em.getReference(RoleEntity.class, id);
            // 创建关联实体
            MemberRoleEntity mr = new MemberRoleEntity();
            mr.setRole(role);
            mr.setMember(mem);
            em.persist(mr);

            // 将关联添加到member实体中
            memRoleCollection.add(mr);
        }
        mem.setMemberRoleCollection(memRoleCollection);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean login(String username, String password) throws UserNotExistsException, PasswordIncorrectException {
        try {
            MemberEntity mem = em.createNamedQuery("memberEntity.findMemberByUsername", MemberEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();

            // check password
            if (false == mem.getPassword().equals(CredentialUtils.sha(password))) {
                throw new PasswordIncorrectException("密码错误");
            }

        } catch (NoResultException ex) {
            throw new UserNotExistsException("用户[" + username + "]不存在");
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends GeneralMemberDto> T findMember(String username, T[] type, boolean isWired) {
        MemberEntity mem = em.createNamedQuery("memberEntity.findMemberByUsername", MemberEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        if (type instanceof StudentDto[]) {
            // 是StudentDto对象
            return (T) makeStudentDto(mem, isWired);
        } else if (type instanceof EmployerDto[]) {
            // 是EmployerDto对象
            return (T) makeEmployerDto(mem, isWired);
        } else if (type instanceof GeneralMemberDto[]) {
            return (T) makeGeneralDto(mem, isWired);
        }

        return null;
    }



    @Override
    @Transactional(readOnly = true)
    public List<GeneralMemberDto> getMemberList(int firstResult, int capacity) {
        int cap = capacity;
        if (0 == capacity) {
            cap = Constants.PAGE_CAPACITY;
        }

        List<MemberEntity> memList = em.createNamedQuery("memberEntity.findAll", MemberEntity.class)
                .setFirstResult(firstResult)
                .setMaxResults(cap)
                .getResultList();

        List<GeneralMemberDto> dtoList = new ArrayList<>();
        for (MemberEntity m : memList) {
            dtoList.add(makeEmployerDto(m, false));
        }

        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMemberAmount() {
        return em.createNamedQuery("memberEntity.count", Long.class)
                .getSingleResult();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public <T extends GeneralMemberDto> boolean updateMember(T memDto) {
        MemberEntity mem = getMemberByUsername(memDto.getUsername());

        if (memDto instanceof StudentDto) {
            StudentDto dto = (StudentDto) memDto;
            updateMemberEntity(mem, dto);
        } else if (memDto instanceof EmployerDto) {
            EmployerDto dto = (EmployerDto) memDto;
            updateMemberEntity(mem, dto);
        }

        em.merge(mem);

        return true;
    }

    @Override
    public boolean deleteMember(Integer memberId) throws UserNotExistsException {
        return false;
    }

    @Override
    public boolean deleteMember(String username) throws UserNotExistsException {
        return false;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public boolean addRole(RoleDto roleDto) {
        RoleEntity role = new RoleEntity(roleDto.getRolename(), roleDto.getMemo());
        em.persist(role);

        return true;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void assignRole(Integer roleId, String username) {
        RoleEntity role = em.getReference(RoleEntity.class, roleId);
        MemberEntity mem = getMemberByUsername(username);

        // 创建关联对象
        MemberRoleEntity mr = new MemberRoleEntity();
        mr.setRole(role);
        mr.setMember(mem);
        em.persist(mr);

        // 创建关联关系
        Collection<MemberRoleEntity> mrCollection = mem.getMemberRoleCollection();
        if (null == mrCollection) {
            mrCollection = new ArrayList<>();
            mem.setMemberRoleCollection(mrCollection);
        }
        mrCollection.add(mr);
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        RoleEntity role = em.getReference(RoleEntity.class, roleId);
        em.remove(role);

        return true;
    }

    @Override
    public void deassignRole(Integer roleId, String username) {
        RoleEntity role = em.getReference(RoleEntity.class, roleId);
        MemberEntity mem = getMemberByUsername(username);

        // 删除关联对象
        MemberRoleEntity targetToDelete = null;
        Collection<MemberRoleEntity> mrCollection = mem.getMemberRoleCollection();
        Iterator<MemberRoleEntity> it = mrCollection.iterator();
        while (it.hasNext()) {
            MemberRoleEntity mr = it.next();
            if (mr.getRole().getRid().equals(roleId)) {
                targetToDelete = mr;
                it.remove();
                break;
            }
        }

        em.remove(targetToDelete);

    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findRole(Integer roleId) {
        RoleEntity role = em.find(RoleEntity.class, roleId);

        return makeRoleDto(role);
    }

    private RoleDto makeRoleDto(RoleEntity role) {
        RoleDto dto = new RoleDto();
        dto.setRolename(role.getRolename());
        dto.setMemo(role.getMemo());

        return dto;
    }

    /**
     * 判断用户名在数据库是是否存在
     * @param username
     * @return
     */
    private boolean isUserExists(String username) {
        Long amount = em.createNamedQuery("memberEntity.isExisted", Long.class)
                .setParameter("username", username)
                .getSingleResult();

        return amount.intValue() == 0 ? false : true;
    }

    /**
     * @deprecated
     */
    private boolean isStudentType(Class clazz) {
        return clazz == StudentDto.class;
    }
    /**
     * @deprecated
     */
    private boolean isEmployerType(Class clazz) {
        return clazz == EmployerDto.class;
    }
    /**
     * @deprecated
     */
    private boolean isGeneralMemberType(Class clazz) {
        return clazz == GeneralMemberDto.class;
    }

    /**
     * 触发role信息的加载
     * @param mem
     * @param dto
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void loadRoleField(MemberEntity mem, GeneralMemberDto dto) {
        // 设置role信息
        // 得到MemberEntity关联的Role对象的id
        Collection<MemberRoleEntity> mrCollection = mem.getMemberRoleCollection();
        if (null != mrCollection) {
            List<Integer> roleIdList = new ArrayList<>();
            for (MemberRoleEntity mr : mrCollection) {
                roleIdList.add(mr.getRole().getRid());
            }

            dto.setRoleIdList(roleIdList);
        }
    }

    private void setGeneralField(MemberEntity mem, GeneralMemberDto dto) {
        dto.setId(mem.getId());
        dto.setUsername(mem.getUsername());
        dto.setEmail(mem.getEmail());
        dto.setName(mem.getName());
        dto.setGender(mem.getGender());
        dto.setVerified(mem.getVerified());
        dto.setProfilePhotoPath(mem.getProfilePhotoPath());
        dto.setPhone(mem.getPhone());
        dto.setAge(mem.getAge());
        dto.setQq(mem.getQq());
    }

    private GeneralMemberDto makeGeneralDto(MemberEntity mem, boolean isWired) {
        GeneralMemberDto dto = new GeneralMemberDto();
        setGeneralField(mem, dto);

        if (isWired) {
            loadRoleField(mem, dto);
        }

        return dto;
    }

    private EmployerDto makeEmployerDto(MemberEntity mem, boolean isWired) {
        EmployerDto dto = new EmployerDto();
        setGeneralField(mem, dto);

        dto.setCompanyName(mem.getCompanyName());

        if (isWired) {
            loadRoleField(mem, dto);
        }

        return dto;
    }

    private StudentDto makeStudentDto(MemberEntity mem, boolean isWired) {
        StudentDto dto = new StudentDto();

        setGeneralField(mem, dto);

        dto.setStudentId(mem.getStudentId());
        dto.setBlockList(mem.getBlockList());

        // 设置教育经历
        // 设置role信息
        if (isWired) {
            Collection<EducationExperienceEntity> eduCollection = mem.getEducationExperienceCollection();
            if (null == eduCollection) {
                eduCollection = new ArrayList<>();
            }

            //List<Integer> schoolIdList = new ArrayList<>();
            List<Integer> academyIdList = new ArrayList<>();
            for (EducationExperienceEntity ee : eduCollection) {
                //Integer schoolId = ee.getSchool().getId();
                Integer academyId = ee.getAcademy().getId();

                //schoolIdList.add(schoolId);
                academyIdList.add(academyId);
            }
            //dto.setSchoolIdList(schoolIdList);
            dto.setAcademyIdList(academyIdList);

            loadRoleField(mem, dto);
        }

        return dto;
    }

    /**
     * 更新实体field，username除外
     * @param mem
     * @param dto
     */
    private void updateMemberEntity(MemberEntity mem, StudentDto dto) {
        // 当dto对象中密码为null时，sha()方法会扔NullPointerException.
        // 这是web-security的一个小bug, 日后修复
        if (dto.getPassword() != null) {
            mem.setPassword(CredentialUtils.sha(dto.getPassword()));
        }
        mem.setEmail(dto.getEmail());
        mem.setName(dto.getName());
        mem.setStudentId(dto.getStudentId());
        mem.setGender(dto.getGender());
        mem.setVerified(dto.getVerified());
        mem.setProfilePhotoPath(dto.getProfilePhotoPath());
        mem.setPhone(dto.getPhone());
        mem.setQq(dto.getQq());
        mem.setAge(dto.getAge());
        mem.setBlockList(dto.getBlockList());
    }
    /**
     * 更新实体field，username除外
     * @param mem
     * @param dto
     */
    private void updateMemberEntity(MemberEntity mem, EmployerDto dto) {
        // 当dto对象中密码为null时，sha()方法会扔NullPointerException.
        // 这是web-security的一个小bug, 日后修复
        if (dto.getPassword() != null) {
            mem.setPassword(CredentialUtils.sha(dto.getPassword()));
        }

        mem.setEmail(dto.getEmail());
        mem.setName(dto.getName());
        //mem.setStudentId(dto.getStudentId());
        mem.setGender(dto.getGender());
        mem.setVerified(dto.getVerified());
        mem.setProfilePhotoPath(dto.getProfilePhotoPath());
        mem.setPhone(dto.getPhone());
        mem.setQq(dto.getQq());
        mem.setAge(dto.getAge());
        mem.setCompanyName(dto.getCompanyName());
        //mem.setBlockList(dto.getBlockList());

    }

    @Transactional(readOnly = true)
    private MemberEntity getMemberByUsername(String username) {
        return em.createNamedQuery("memberEntity.findMemberByUsername", MemberEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    /**
     * For test purpose only
     * @param args
     */
    public static void main(String[] args) {
        String hashed = CredentialUtils.sha("111111");
        Print.print(hashed);
    }
}
