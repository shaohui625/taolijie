package com.fh.taolijie.domain;

public class ApplicationIntendModel {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resume_job_post_category.resume_id
     *
     * @mbggenerated
     */
    private Integer resumeId;
    private ResumeModel resume;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resume_job_post_category.job_post_category_id
     *
     * @mbggenerated
     */
    private Integer jobPostCategoryId;
    private JobPostCategoryModel jobPost;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resume_job_post_category.resume_id
     *
     * @return the value of resume_job_post_category.resume_id
     *
     * @mbggenerated
     */
    public Integer getResumeId() {
        return resumeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resume_job_post_category.resume_id
     *
     * @param resumeId the value for resume_job_post_category.resume_id
     *
     * @mbggenerated
     */
    public void setResumeId(Integer resumeId) {
        this.resumeId = resumeId;
    }

    public ResumeModel getResume() {
        return resume;
    }

    public void setResume(ResumeModel resume) {
        this.resume = resume;
    }

    public JobPostCategoryModel getJobPost() {
        return jobPost;
    }

    public void setJobPost(JobPostCategoryModel jobPost) {
        this.jobPost = jobPost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resume_job_post_category.job_post_category_id
     *
     * @return the value of resume_job_post_category.job_post_category_id
     *
     * @mbggenerated
     */
    public Integer getJobPostCategoryId() {
        return jobPostCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resume_job_post_category.job_post_category_id
     *
     * @param jobPostCategoryId the value for resume_job_post_category.job_post_category_id
     *
     * @mbggenerated
     */
    public void setJobPostCategoryId(Integer jobPostCategoryId) {
        this.jobPostCategoryId = jobPostCategoryId;
    }
}