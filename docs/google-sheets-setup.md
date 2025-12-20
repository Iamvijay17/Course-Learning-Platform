# Google Sheets Development Tracking Setup

This guide will help you set up a professional development tracking spreadsheet in Google Sheets with colors, dropdowns, and automated calculations.

## Step 1: Create New Google Sheet

1. Go to [Google Sheets](https://sheets.google.com)
2. Create a new blank spreadsheet
3. Name it "Course Platform Development Tracking"

## Step 2: Import CSV Data

1. Copy the data from `development-tracking.csv`
2. Paste it into your Google Sheet (starting from cell A1)
3. The headers should be in row 1

## Step 3: Format Headers

### Header Row Formatting
1. **Select row 1** (header row)
2. **Background color**: Dark blue (#1976D2)
3. **Text color**: White
4. **Font**: Arial, 12pt, Bold
5. **Text alignment**: Center
6. **Border**: All borders, black, 1pt

### Column Widths
Set appropriate column widths:
- A (Phase): 200px
- B (Week): 60px
- C (Task ID): 100px
- D (Task Name): 250px
- E (Description): 300px
- F (Assigned To): 150px
- G (Status): 120px
- H (Priority): 80px
- I (Estimated Hours): 120px
- J (Start Date): 100px
- K (End Date): 100px
- L (Actual Start): 100px
- M (Actual End): 100px
- N (Progress %): 100px
- O (Dependencies): 200px
- P (Notes): 250px
- Q (Blocker): 150px

## Step 4: Data Validation Dropdowns

### Status Column (G)
1. Select column G (Status)
2. Go to **Data → Data Validation**
3. Criteria: **List of items**
4. Enter: `Not Started,In Progress,Completed,Blocked,On Hold`
5. Check "Show dropdown list in cell"
6. Check "Show validation help text"

### Priority Column (H)
1. Select column H (Priority)
2. Go to **Data → Data Validation**
3. Criteria: **List of items**
4. Enter: `Low,Medium,High,Critical`
5. Check "Show dropdown list in cell"

### Assigned To Column (F)
1. Select column F (Assigned To)
2. Go to **Data → Data Validation**
3. Criteria: **List of items**
4. Enter: `Backend Developer,Frontend Developer,DevOps Engineer,QA Engineer,Database Administrator,Technical Lead,UI/UX Designer,API Developer,Security Specialist,Performance Engineer,Product Manager,Support Engineer,Project Manager`
5. Check "Show dropdown list in cell"

## Step 5: Conditional Formatting

### Status-Based Row Coloring
1. Select all data rows (A2:Q)
2. Go to **Format → Conditional formatting**

#### Rule 1: Not Started (Light Gray)
- **Apply to range**: A2:Q
- **Format cells if**: Custom formula is `=$G2="Not Started"`
- **Background color**: Light gray (#F5F5F5)

#### Rule 2: In Progress (Light Blue)
- **Apply to range**: A2:Q
- **Format cells if**: Custom formula is `=$G2="In Progress"`
- **Background color**: Light blue (#E3F2FD)

#### Rule 3: Completed (Light Green)
- **Apply to range**: A2:Q
- **Format cells if**: Custom formula is `=$G2="Completed"`
- **Background color**: Light green (#E8F5E8)

#### Rule 4: Blocked (Light Red)
- **Apply to range**: A2:Q
- **Format cells if**: Custom formula is `=$G2="Blocked"`
- **Background color**: Light red (#FFEBEE)

#### Rule 5: On Hold (Light Yellow)
- **Apply to range**: A2:Q
- **Format cells if**: Custom formula is `=$G2="On Hold"`
- **Background color**: Light yellow (#FFFDE7)

### Priority-Based Text Coloring
#### High Priority (Red Text)
- **Apply to range**: H2:H
- **Format cells if**: Custom formula is `=$H2="High"`
- **Text color**: Red (#D32F2F)

#### Critical Priority (Bold Red)
- **Apply to range**: H2:H
- **Format cells if**: Custom formula is `=$H2="Critical"`
- **Text color**: Dark red (#B71C1C)
- **Font weight**: Bold

### Progress Bar Visualization
1. Select column N (Progress %)
2. Go to **Format → Conditional formatting**

#### Progress Ranges
- **0%**: Red background (#FFCDD2)
- **1-25%**: Orange background (#FFE0B2)
- **26-50%**: Yellow background (#FFF9C4)
- **51-75%**: Light green background (#C8E6C9)
- **76-99%**: Green background (#A5D6A7)
- **100%**: Dark green background (#4CAF50)

## Step 6: Formulas and Calculations

### Add Summary Dashboard (New Sheet)

1. Create a new sheet named "Dashboard"
2. Add the following formulas:

#### Total Tasks
```
=COUNT('Task Tracker'!A:A) - 1
```

#### Completed Tasks
```
=COUNTIF('Task Tracker'!G:G, "Completed")
```

#### In Progress Tasks
```
=COUNTIF('Task Tracker'!G:G, "In Progress")
```

#### Blocked Tasks
```
=COUNTIF('Task Tracker'!G:G, "Blocked")
```

#### Overall Progress (%)
```
=ROUND(('Task Tracker'!N:N * 'Task Tracker'!I:I) / SUM('Task Tracker'!I:I), 2)
```

#### Tasks by Phase
```
=COUNTIFS('Task Tracker'!A:A, "Phase 1:*", 'Task Tracker'!G:G, "Completed")
```

#### Tasks by Assignee
```
=COUNTIFS('Task Tracker'!F:F, "Backend Developer", 'Task Tracker'!G:G, "Completed")
```

#### Overdue Tasks
```
=COUNTIFS('Task Tracker'!K:K, "<"&TODAY(), 'Task Tracker'!G:G, "<>Completed", 'Task Tracker'!G:G, "<>Blocked")
```

## Step 7: Charts and Visualizations

### Progress Overview Chart
1. In Dashboard sheet, create a pie chart:
   - Data range: Status counts
   - Chart type: Pie chart
   - Title: "Task Status Distribution"

### Progress by Phase Chart
1. Create a bar chart:
   - Data range: Phase completion percentages
   - Chart type: Stacked bar chart
   - Title: "Progress by Phase"

### Burndown Chart
1. Create a line chart:
   - X-axis: Weeks
   - Y-axis: Remaining tasks
   - Title: "Sprint Burndown"

## Step 8: Advanced Features

### Data Filters
1. Select header row
2. Go to **Data → Create a filter**
3. Use filters to view tasks by:
   - Phase
   - Week
   - Assignee
   - Status
   - Priority

### Protected Ranges
1. Protect the formula columns to prevent accidental edits
2. Go to **Data → Protected sheets and ranges**
3. Protect columns with calculations

### Automated Updates
1. Set up automatic email notifications for blocked tasks
2. Create weekly progress report generation

## Step 9: Custom Views

### Create Named Views
1. **All Tasks**: Default view
2. **My Tasks**: Filter by current user
3. **This Week**: Filter by current week
4. **Blocked Tasks**: Show only blocked items
5. **High Priority**: Show critical and high priority tasks

## Step 10: Mobile Access

### Enable Mobile Editing
1. Share the sheet with team members
2. Set appropriate permissions (Edit access)
3. Enable offline editing in Google Sheets mobile app

## Usage Instructions

### Daily Updates
1. Update task status as work progresses
2. Record actual start/end dates
3. Update progress percentages
4. Note any blockers or issues

### Weekly Reviews
1. Review dashboard metrics
2. Identify bottlenecks and blocked tasks
3. Adjust timelines and priorities as needed
4. Generate progress reports

### Reporting
1. Use charts for visual progress tracking
2. Export data for stakeholder presentations
3. Generate burndown charts for sprint reviews

## Color Scheme Reference

| Status | Background | Text | Description |
|--------|------------|------|-------------|
| Not Started | #F5F5F5 | #000000 | Light gray |
| In Progress | #E3F2FD | #000000 | Light blue |
| Completed | #E8F5E8 | #000000 | Light green |
| Blocked | #FFEBEE | #000000 | Light red |
| On Hold | #FFFDE7 | #000000 | Light yellow |

| Priority | Text Color | Font Weight |
|----------|------------|-------------|
| Low | #000000 | Normal |
| Medium | #000000 | Normal |
| High | #D32F2F | Normal |
| Critical | #B71C1C | Bold |

## Troubleshooting

### Common Issues
1. **Dropdowns not working**: Check data validation settings
2. **Conditional formatting not applying**: Verify formula syntax
3. **Charts not updating**: Refresh data range selections
4. **Mobile editing issues**: Check sharing permissions

### Performance Tips
1. Limit conditional formatting rules to essential ones
2. Use array formulas sparingly on large datasets
3. Archive completed tasks to separate sheet after 6 months

## Template Download

You can also create a copy of this template by:
1. Opening the provided CSV data
2. Following the setup instructions above
3. Saving as a template for future projects

This Google Sheets setup provides a professional, collaborative environment for tracking your development progress with visual indicators, automated calculations, and real-time updates.
