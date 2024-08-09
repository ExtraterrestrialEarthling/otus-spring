const SKILLS_API_BASE_URL = "/skills";

export const SKILLS_API_ENDPOINTS = {
    getSkills: `${SKILLS_API_BASE_URL}`,
    getSkillById: (id) => `${SKILLS_API_BASE_URL}/${id}`,
    createSkill: `${SKILLS_API_BASE_URL}`,
    updateSkill: (id) => `${SKILLS_API_BASE_URL}/${id}`,
    deleteSkill: (id) => `${SKILLS_API_BASE_URL}/${id}`
};
