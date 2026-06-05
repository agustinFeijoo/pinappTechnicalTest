export interface SectionListItem {
  sectionId: number;
  sectionName: string;
  gradeName: string;
}

export const FALLBACK_SECTIONS: SectionListItem[] = [
  { sectionId: 1, sectionName: '3A', gradeName: '3rd Grade' },
  { sectionId: 2, sectionName: '3B', gradeName: '3rd Grade' },
  { sectionId: 3, sectionName: '4A', gradeName: '4th Grade' },
  { sectionId: 4, sectionName: '4B', gradeName: '4th Grade' }
];
