# AI Task Documents
The purpose of this document is to provide instructions for an AI on how to create one or several task documents.

Task documents reside in the directory `doc/tasks`

# Creating a task document
Simpler tasks just require one document.

Each task must be stand alone. This means that after a task is completed:
- the problem described should be fully solved
- the code compiles and the tests pass


Use the [task_template.md](task_template.md) as a starting point. Copy it to `doc/tasks/[task_name].md` and fill in the details.

Instructions for writing a task document:
- Be concise
- Follow the template structure exactly

# Creating multiple task documents
More complex tasks can be broken down into several documents.
Several less complicated tasks are preferred over a single complex task.

Use the [overview_template.md](overview_template.md) for the overview document that coordinates all sub-tasks.

## File naming convention:
- Overview: `[task_name]_overview.md`
- Sub-tasks: `[task_name]_task_[n]_[sub_task_name].md`

## Example:
- annotations_overview.md
- annotations_task_1_interfaces.md
- annotations_task_2_response.md
- annotations_task_3_request.md
